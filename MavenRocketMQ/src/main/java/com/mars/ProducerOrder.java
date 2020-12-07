package com.mars;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.ArrayList;
import java.util.List;

public class ProducerOrder {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("xxooGPOrder");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        for (int i = 1; i <= 20; i++) {
            Message message = new Message("xxooTopicOrder", ("msg at " + i).getBytes());
            producer.send(message, new MessageQueueSelector() {
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    return list.get(0);
                }
            }, null);
        }
        producer.shutdown();
    }
}
