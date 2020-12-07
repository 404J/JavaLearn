package com.mars;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;

public class ProducerTransaction {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("xxooGP");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        producer.shutdown();
    }
}
