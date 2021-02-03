# Day01

## VFS

虚拟文件系统，目录树，树上的节点可以映射到具体的物理位置。属于解耦层，向用户程序提供接口

> 使用 df 查看分区和目录树的映射。使用 mount、unmount 进行挂载和卸载

## file describe

进程隔离，用户程序使用 fd 的 指针 seek 获取同一个 pagecache 文件的不同位置数据

> 软链接和硬链接的区别，硬链接都为磁盘文件的引用，删除某个引用不影响另一个，软链接是引用的引用，删除磁盘引用会影响。
> 可以通过 stat 查看文件信息（Inode）
> 通过 lsof -p $$ 查看进程打开了哪些文件

## Linux 常见的知识

### socket 文件类型

1. echo $$ 获取当前 bash 进程
2. cd /proc/[pid]/fd 查看 fd 目录
3. exec 3<> /dev/tcp/wwww.baidu.com/80
4. ll /proc/[pid]/fd 查看 fd 目录, 可见 socket
5. lsof -p $$ 查看进程打开了哪些文件

### 重定向输入输出流

- 重定向标准输出到文件

```shell
ls ./ 1> ~/ls.out
```

```shell
ls ./ ./ooxx  1> ls.out 2>& 1
```

- 重定向标准输入到文件

```shell
ls 0< ls.in 1> ls.out
```

### 管道 ｜

- 显示文件的第n行

```shell
head -n [file] | tail -1
```

> 管道的左右两边是两个子进程

### 父子进程

1. x=1 创建变量
2. export x 导出变量
3. /bin/bash 创建子进程
4. echo $x 输出变量

### 显示 pipe 文件类型

1. { echo $BASHPID; read x; } | cat 创建父子进程进行阻塞
2. 重新打开 bash , 执行 ps -ef | grep [父进程pid] 查看相应的子进程
3. ll /proc/[子进程pid]/fd
4. lsof -p [子进程pid] 查看两个子进程的 pipe 文件的 inode 是否一致

## PageCache

默认为 4k, 用于缓存数据到内存，提供给用户程序使用，修改的 pagecache 会 flush 到磁盘
