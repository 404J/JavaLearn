package com.mars;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class ProducerOneWay {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("xxooGP");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        Message message = new Message("xxooTopic", "tag-b", "this is one way hello world tag BBB".getBytes());
        producer.sendOneway(message);
        producer.shutdown();
    }
}
