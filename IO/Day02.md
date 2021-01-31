# Day02

## PageCache

由内核维护，磁盘和内存的中间层，当文件进行更新的时候，PageCache 产生脏页，需要 flush 到磁盘，使用 `sysctl -a | grep dirty` 可以查看 flush 的策略。PageCache 具有淘汰策略，许久没用的会被新的淘汰掉。当用户程序进行文件写的时候，如果 PageCache 的 flush 测试配置的不当，当系统断电的时候，文件数据可能会丢失。

> java 中使用普通的 OutputStream 要比 BufferOutputStream 慢得多。因为 BufferOutputStream 会在 JVM 中维护一个 8KB 的缓存，当缓存写满了才会进行 SysCall 调用内核写

## ByteBuffer
