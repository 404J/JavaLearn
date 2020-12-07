package com.mars;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;

public class Producer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("xxooGP");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        List<Message> messages = new ArrayList<Message>();
        for (int i = 1; i <= 100; i++) {
            Message message = new Message("xxooTopic", ("age at " + i).getBytes());
            message.putUserProperty("age", String.valueOf(i));
            messages.add(message);
        }
        producer.send(messages);
        producer.shutdown();
    }
}
