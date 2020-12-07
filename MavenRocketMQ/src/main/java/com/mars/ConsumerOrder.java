package com.mars;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class ConsumerOrder {
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("xxooCGOrder");
        consumer.setNamesrvAddr("localhost:9876");
        consumer.subscribe("xxooTopicOrder", "*");
        consumer.registerMessageListener(new MessageListenerOrderly() {
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                for(MessageExt messageExt : list) {
                    System.out.println(new String(messageExt.getBody()) + ", Thread: " + Thread.currentThread().getName());
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
    }
}
