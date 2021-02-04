# Day04

## 网络 IO 模型

### NIO 单线程模型

> NIO

1. JDK: new IO
2. OS: NonBlocking

主线程接收请求和轮询连接是否有数据

- socket(PF_INET, SOCK_STREAM, IPPROTO_IP) = 4 --------> 获取 socket 的 fd
- setsockopt(4, SOL_SOCKET, SO_REUSEADDR, [1], 4) = 0
- bind(4, {sa_family=AF_INET, sin_port=htons(9090), sin_addr=inet_addr("0.0.0.0")}, 16) = 0 --------> bind 9090
- listen(4, 50) --------> 监听 9090
- fcntl(4, F_SETFL, O_RDWR|O_NONBLOCK)    = 0 --------> 使得 accept 为非阻塞
- accept(4, 0x7f54600ce8b0, 0x7f5467f8568c) = -1 EAGAIN (Resource temporarily unavailable) --------> 非阻塞返回无连接
- accept(4, {sa_family=AF_INET, sin_port=htons(54424), sin_addr=inet_addr("172.20.0.3")}, [16]) = 5 --------> 非阻塞返回有连接
- fcntl(5, F_SETFL, O_RDWR|O_NONBLOCK)    = 0 --------> 使得 read 为非阻塞
- read(5, "123\n", 4096)                  = 4 --------> 非阻塞返回 socket 有数据
