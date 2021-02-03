# Day03

## 网络 IO/TCP

> 监控进程系统调用：strace -ff -o out.txt cmd

### 实验

- 准备 socket server 并启动，阻塞在 accept
- 使用 tcpdump 抓包：tcpdump -nn -i eth0 port [port]
- 使用 netstat 监控网络：netstat -natp（只有一个监听的记录）
- 准备 socket client 并启动
- tcpdump 检测到 tcp 三次握手
- netstat 记录中多了一个 socket, 内核开辟资源，但是没有分配给任何进程
- 客户端发送数据
- netstat 记录中的 Recv-Q 接收到字节数据
- socket server 进行 accept
- socket 被分配，且 lsof -p [pid] 使用该 socket 的 fd

> socket 是一个四元组（cip:cport <==> sip:sport）四个元素进行区分，表示唯一性
> 每个 socket 维护一个读写的 buffer

### Server Socket 参数

- BACK_LOG: 备胎的个数，当完成 tcp 的三次握手后，没有进行 accept 之前，允许内核暂存的连接个数

### Socket 参数

- TcpNoDelay: 客户端是否延迟堆积一定的数据包一次性发送
- CLI_KEEPALIVE: 是否主动发送心跳包

## 网络 IO 模型

### 传统 BIO 多线程模型

主线程接收 socket 连接（accept() 为阻塞方法）：

- socket(PF_INET, SOCK_STREAM, IPPROTO_IP) = 5
- bind(5, {sa_family=AF_INET, sin_port=htons(9090), sin_addr=inet_addr("0.0.0.0")}, 16) = 0
- listen(5, 50)
- accept(5, {sa_family=AF_INET, sin_port=htons(54422), sin_addr=inet_addr("172.20.0.3")}, [16]) = 6
- clone(child_stack=0x7f1f01430fb0, flags=CLONE_VM|CLONE_FS|CLONE_FILES|CLONE_SIGHAND|CLONE_THREAD|CLONE_SYSVSEM|CLONE_SETTLS|CLONE_PARENT_SETTID|CLONE_CHILD_CLEARTID, parent_tidptr=0x7f1f014319d0, tls=0x7f1f01431700, child_tidptr=0x7f1f014319d0) = 6756

从线程读取 socket 的数据（recvfrom() 为阻塞方法）:

- recvfrom(6, "hello\n", 8192, 0, NULL, NULL) = 6
