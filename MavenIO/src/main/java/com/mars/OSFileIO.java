package com.mars;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class OSFileIO {
    static byte[] data = "123456789\n".getBytes();
    static String path = "/Users/a404/out.txt";

    public static void main(String[] args) throws Exception {
//        basicFileIO();
//        bufferedFileIO();
        randomAccessFileIO();
    }

    public static void basicFileIO() throws Exception {
        // 速度慢，频繁 syscall
        File file = new File(path);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        while (true) {
            Thread.sleep(10);
            fileOutputStream.write(data);
        }
    }

    public static void bufferedFileIO() throws Exception {
        // JVM 维护一个 8KB 的buffer，然后 syscall 将 buffer 写入 page cache
        File file = new File(path);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        while (true) {
            Thread.sleep(10);
            bufferedOutputStream.write(data);
        }
    }

    public static void randomAccessFileIO() throws Exception {
        // 使用 seek 可以读写文件的任意位置
        RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw");
        System.out.println(randomAccessFile.getFilePointer());
        randomAccessFile.write(data);
        randomAccessFile.write(data);
        System.out.println(randomAccessFile.getFilePointer());
        System.out.println("---------------- write at begin ----------------");
        System.in.read();

        randomAccessFile.seek(5);
        randomAccessFile.write("ooxx".getBytes());
        System.out.println(randomAccessFile.getFilePointer());
        System.out.println("---------------- seek write ----------------");
        System.in.read();

        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
        map.put("map put".getBytes());
        System.out.println("---------------- map put ----------------");
        map.force();
        System.in.read();

        ByteBuffer buffer = ByteBuffer.allocate(2048);
        int read = channel.read(buffer);
        System.out.println(buffer);
        buffer.flip();
        System.out.println(buffer);
        for (int i = 0; i < buffer.limit(); i++) {
            Thread.sleep(200);
            System.out.print(((char)buffer.get(i)));
        }
    }
}
