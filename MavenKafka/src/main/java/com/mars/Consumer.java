package com.mars;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

public class Consumer {
    public static void main(String[] args) {
        String topic = "maven-kafka";

        Properties properties = new Properties();
        // 基础配置
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // Group 的配置
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "g5");

        // offset 的配置, 当 Consumer 第一次启动的时候 offset 的获取策略
        // 可选参数：
        // 1. earliest
        // 2. latest
        // 3. none
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // offset commit 的配置
        // 是否开启 offset 的自动异步提交，可能会造成消息的重复消费或者丢失
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        // 自动提交 offset 的时间间隔
//        properties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
        // 订阅 topic
        kafkaConsumer.subscribe(Arrays.asList(topic));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(0));
            if (records.isEmpty()) {
                continue;
            }
            System.out.println("---------------- poll msg " + records.count() + " ----------------" );
            // 第一种方案维护 offset ---> 单线程逐个按照消息处理
//            records.forEach((record) -> {
//                int partition = record.partition();
//                long offset = record.offset();
//                String value = record.value();
//                String key = record.key();
//                System.out.println("Msg get: " + " key: " + key +  " value: " + value + " partition: " + partition + " offset: " + offset);
//
//                TopicPartition topicPartition = new TopicPartition(topic, partition);
//                OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(offset);
//                HashMap<TopicPartition, OffsetAndMetadata> map = new HashMap<>();
//
//                map.put(topicPartition, offsetAndMetadata);
//                kafkaConsumer.commitSync(map); // 逐条维护 offset 可靠性高，效率低
//            });
            // 第二种方案维护 offset ---> 按批次进行维护
//            kafkaConsumer.commitSync(); // 不同的 partition 的消息没有必要一起维护
            // 第三种方案维护 offset ---> 根据不同的 partition 维护 offset，多线程方案
            Set<TopicPartition> topicPartitionSet = records.partitions();
            if (topicPartitionSet.isEmpty()) {
                return;
            }
            topicPartitionSet.forEach(topicPartition -> {
                List<ConsumerRecord<String, String>> recordsByPartition = records.records(topicPartition);
                if (recordsByPartition.isEmpty()) {
                    return;
                }
                recordsByPartition.forEach(record -> {
                    System.out.println("Msg get: " + " key: " + record.key()
                            +  " value: " + record.value()
                            + " partition: " + record.partition()
                            + " offset: " + record.offset());
                });

                long offset = recordsByPartition.get(recordsByPartition.size() - 1).offset();
                OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(offset);
                HashMap<TopicPartition, OffsetAndMetadata> map = new HashMap<>();

                map.put(topicPartition, offsetAndMetadata);
                kafkaConsumer.commitSync(map); // offset 是面向 partition 的，分开维护可以利用多线程高性能编程
            });
        }

    }
}
