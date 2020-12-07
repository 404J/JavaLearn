package com.mars;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.ArrayList;
import java.util.List;

public class ProducerTransaction {
    public static void main(String[] args) throws Exception {
        final int[] flag = {1};
        TransactionMQProducer producer = new TransactionMQProducer("transPG");
        producer.setNamesrvAddr("localhost:9876");
        producer.setTransactionListener(new TransactionListener() {
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                System.out.println("executeLocalTransaction");
                System.out.println("message: " + new String(message.getBody()));
                System.out.println("transactionId: " + message.getTransactionId());
                System.out.println("execute some methods...");
                return LocalTransactionState.UNKNOW;
            }

            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                System.out.println("checkLocalTransaction");
                System.out.println("message: " + new String(messageExt.getBody()));
                System.out.println("transactionId: " + messageExt.getTransactionId());
                if (flag[0] == 2) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                flag[0] = flag[0] + 1;
                return LocalTransactionState.UNKNOW;
            }
        });
        producer.start();
        Message message = new Message("test-topic", "This is transaction msg".getBytes());
        producer.sendMessageInTransaction(message, null);
    }
}
