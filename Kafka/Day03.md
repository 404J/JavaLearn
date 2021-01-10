# Day03

## Kafka Java API

```java
public static void main(String[] args) throws ExecutionException, InterruptedException {
  String topic = "maven-kafka";
  Properties properties = new Properties();
  properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
  properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
  properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

  KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);

  while (true) {
      for (int i = 0; i < 3; i++) { // 区分消息的内容
          for (int j = 0; j < 3; j++) { // 区分消息的类别，根据 key 区分，使其发送到同一个 Partition
              ProducerRecord<String, String> record = new ProducerRecord<>(topic, "msg" + j, "val" + i);
              Future<RecordMetadata> send = kafkaProducer.send(record);
              RecordMetadata recordMetadata = send.get();
              int partition = recordMetadata.partition();
              long offset = recordMetadata.offset();
              System.out.println("Msg sent: " + "value: " + record.value() + " partition: " + partition + " offset: " + offset);
          }
      }
  }
}
```

## 一个 Consumer 消费多个 Partition 时的 offset 的维护方案

![一个 Consumer 消费多个 Partition 时的 offset 的维护方案](../images/kafka03.svg)
