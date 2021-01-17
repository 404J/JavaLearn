package com.mars;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String topic = "test";

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "0");
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);

        while (true) {
            for (int i = 0; i < 3; i++) { // 区分消息的内容
                for (int j = 0; j < 3; j++) { // 区分消息的类别，根据 key 区分，使其发送到同一个 Partition
                    ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, "msg-" + j, "val-" + i);
                    Future<RecordMetadata> send = kafkaProducer.send(record);
                    RecordMetadata recordMetadata = send.get();
                    int partition = recordMetadata.partition();
                    long offset = recordMetadata.offset();
                    System.out.println("Msg sent: " + " key: " + record.key() +  " value: " + record.value() + " partition: " + partition + " offset: " + offset);
                }
            }
        }
    }
}
