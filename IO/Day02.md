# Day02

## PageCache

由内核维护，磁盘和内存的中间层，当文件进行更新的时候，PageCache 产生脏页，需要 flush 到磁盘，使用 `sysctl -a | grep dirty` 可以查看 flush 的策略。PageCache 具有淘汰策略，许久没用的会被新的淘汰掉。当用户程序进行文件写的时候，如果 PageCache 的 flush 测试配置的不当，当系统断电的时候，文件数据可能会丢失。

> java 中使用普通的 OutputStream 要比 BufferOutputStream 慢得多。因为 BufferOutputStream 会在 JVM 中维护一个 8KB 的缓存，当缓存写满了才会进行 SysCall 调用内核写

## ByteBuffer

```java
    public static void whatIsByteBuffer() {
//        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        byteBuffer.put("abcdef".getBytes());
        System.out.println("---------- put abcdef ----------");
        System.out.println("buffer: " + byteBuffer);

        byteBuffer.flip(); // 读写切换
        System.out.println("---------- flip ----------");
        System.out.println("buffer: " + byteBuffer);

        byte b = byteBuffer.get();
        System.out.println("---------- get " + new String(new byte[]{b}) + " ----------");
        System.out.println("buffer: " + byteBuffer);

        byteBuffer.compact();
        System.out.println("---------- compact ----------");
        System.out.println("buffer: " + byteBuffer);

    }
```

## bufferedFileIO

```java
    public static void bufferedFileIO() throws Exception {
        // JVM 维护一个 8KB 的buffer，然后 syscall 将 buffer 写入 page cache
        File file = new File(path);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        while (true) {
            Thread.sleep(10);
            bufferedOutputStream.write(data);
        }
    }
```

## randomAccessFileIO

```java
    public static void randomAccessFileIO() throws Exception {
        // 使用 seek 可以读写文件的任意位置
        RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw");
        System.out.println(randomAccessFile.getFilePointer());
        randomAccessFile.write(data);
        randomAccessFile.write(data);
        System.out.println(randomAccessFile.getFilePointer());
        System.out.println("---------------- write at begin ----------------");
        System.in.read();

        // lseek
        randomAccessFile.seek(5);
        randomAccessFile.write("ooxx".getBytes());
        System.out.println(randomAccessFile.getFilePointer());
        System.out.println("---------------- seek write ----------------");
        System.in.read();

        FileChannel channel = randomAccessFile.getChannel();

        // mmap(NULL, 1024, PROT_READ|PROT_WRITE, MAP_SHARED, 4, 0) = 0x7f60f839a000
        // 在虚拟文件系统开辟一个映射到文件的空间，写的时候不会有系统调用，受内核 pagecache 约束
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
        map.put("map put".getBytes());
        System.out.println("---------------- map put ----------------");
        // msync(0x7f60f839a000, 1024, MS_SYNC)    = 0
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
```
