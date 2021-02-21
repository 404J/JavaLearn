# Day06

## 多路复用器 - 单线程 - 单 selector 模型（SocketMultiplexingIOServerV1）

在单个线程中进行 accept read 和 write 操作，没有利用 cpu 多线程的处理能力。如果某个 fd 的 accept 或者 R/W 阻塞过慢会影响其他 fd 的处理

## 多路复用器 - 多线程 - 单 selector 模型（SocketMultiplexingIOServerV2）

主线程进行判断哪些 fd 是否 ready，然后多线程进行 R/W 操作，但是 R/W 之前需要 cancel, 要不然主线程进行下一次 select 的时候仍会返回之前处理过的 fd, 造成重复的处理。其中 cancel 同样是 syscall(epoll_ctl)

## 多路复用器 - 多线程 - 多 selector 模型 （simpleNetty）

每个线程对应一个 selector, selector 的个数为对应的 cpu 核数，每个线程内部为顺序执行，进行 accept 和 R/W 操作，避免了 SocketMultiplexingIOServerV2 中的频繁的 cancel 系统调用。
