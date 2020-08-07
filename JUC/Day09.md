# Day09

## Disruptor

- 存储方式
  环形 Buffer 队列，使用数组实现，采用覆盖旧元素，不用维护首尾指针，维护一个指向下一个可用位置的 sequence，sequence = num & (size - 1)

  ```java
  public class App {
    public static void handleEvent(MyEvent event, long sequence, boolean endOfBatch) {
        System.out.println(event);
    }

    public static void translate(MyEvent event, long sequence, ByteBuffer buffer) {
        event.setEvent(buffer.getLong(0));
    }

    public static void main(String[] args) throws Exception {
      // Specify the size of the ring buffer, must be power of 2.
      int bufferSize = 1024;

      // Construct the Disruptor
      Disruptor<MyEvent> disruptor = new Disruptor<>(MyEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);
      // Connect the handler
      disruptor.handleEventsWith(App::handleEvent);
      // Start the Disruptor, starts all threads running
      disruptor.start();
      // Get the ring buffer from the Disruptor to be used for publishing.
      RingBuffer<MyEvent> ringBuffer = disruptor.getRingBuffer();

      ByteBuffer bb = ByteBuffer.allocate(8);
      for (long l = 0; true; l++) {
        bb.putLong(0, l);
        ringBuffer.publishEvent(App::translate, bb);
        Thread.sleep(1000);
      }
    }
  }
  ```
