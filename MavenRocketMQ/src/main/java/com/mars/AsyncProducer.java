package com.mars;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class AsyncProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer asyncProducer = new DefaultMQProducer("xxooGP");
        asyncProducer.setNamesrvAddr("localhost:9876");
        asyncProducer.start();
        Message message = new Message("xxooTopic", "this is async hello world".getBytes());
        asyncProducer.send(message, new SendCallback() {
            public void onSuccess(SendResult sendResult) {
                System.out.println(sendResult);
            }

            public void onException(Throwable throwable) {
                System.out.println("throw Exception");
            }
        });
//        asyncProducer.shutdown();
    }
}
