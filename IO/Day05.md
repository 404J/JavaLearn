# Day05

## NIO 单线程模型的问题

连接数过多的时候，app 进行轮询，频繁的系统调用，但是只有有限的 socket 有数据。效率低下，成本高

## 多路复用器(同步非阻塞)

通过一次系统调用，得知多个 IO 的状态

- select
- poll
- epoll

### SELECT(synchronous I/O multiplexing)

传递多个 fds, 获取到哪些 fd 是 ready，但是个数是有限制的

> 内核如何知道 fd 是否有数据？ 当有 IO 的时候，会产生 IO中断，此时 CPU 进行处理，内核收到 callback，进行 fd 的 buffer 处理，然后调用 select 时候会进行轮询判断是否有 buffer，进而返回哪些 fd 可读

### POLL

同 SELECT，但是没有数量限制

> fd 的轮询放到了内核中进行，依然需要进行大量的轮询。但是相比与 NIO 减少了 syscall。每次都会进行大量的 fd 的 copy

### EPOLL

EPOLL 是 POLL 的升级版本，通过 epoll_create 返回一个 epfd, 然后调用 epoll_ctl 向内核传递一个 fd, 该 fd 放到一个红黑树中，当有 IO 中断的时候，内核处理完相关 buffer 后，并把该从红黑树中的 fd 放到一个 链表中，调用 epoll_wait 拿到哪些 fd 是可读的，就不会有大量的轮询
