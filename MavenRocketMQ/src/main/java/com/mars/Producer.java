package com.mars;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class Producer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("xxooGP");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        Message message = new Message("xxooTopic", "this is hello world".getBytes());
        SendResult sendResult = producer.send(message);
        System.out.println(sendResult);
        producer.shutdown();
    }
}
