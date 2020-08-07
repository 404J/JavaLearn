package mars;

import java.nio.ByteBuffer;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.lmax.disruptor.dsl.Disruptor;


public class App {
    public static void handleEvent(MyEvent event, long sequence, boolean endOfBatch)
    {
        System.out.println(event);
    }

    public static void translate(MyEvent event, long sequence, ByteBuffer buffer)
    {
        event.setEvent(buffer.getLong(0));
    }

    public static void main(String[] args) throws Exception
    {
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
        for (long l = 0; true; l++)
        {
            bb.putLong(0, l);
            ringBuffer.publishEvent(App::translate, bb);
            Thread.sleep(1000);
        }
    }
}
