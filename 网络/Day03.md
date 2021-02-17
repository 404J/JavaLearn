# Day03

## LVS DR 模型 负载均衡器 搭建

node01:
  ifconfig ens33:1 192.168.63.100/24
node02~node03:
  1)修改内核：
    echo 1 > /proc/sys/net/ipv4/conf/ens33/arp_ignore
    echo 1 > /proc/sys/net/ipv4/conf/all/arp_ignore
    echo 2 > /proc/sys/net/ipv4/conf/ens33/arp_announce
    echo 2 > /proc/sys/net/ipv4/conf/all/arp_announce
  2设置隐藏的vip：
    ifconfig lo:1 192.168.63.100 netmask 255.255.255.255

RS中的服务：
node02~node03:
  yum install httpd -y
  service httpd start
  vim /var/www/html/index.html
    from 192.168.63.x
  firewall-cmd --add-service=http --permanent
  firewall-cmd --add-port=80/tcp --permanent
  firewall-cmd --reload

LVS服务配置:
node01:
  yum install -y ipvsadm
  ipvsadm -A -t 192.168.63.100:80 -s rr
  ipvsadm -a -t 192.168.63.100:80 -r 192.168.63.4 -g -w 1
  ipvsadm -a -t 192.168.63.100:80 -r 192.168.63.5 -g -w 1
  ipvsadm -ln

验证：
  浏览器访问 192.168.63.100 看到负载 疯狂F5
  node01：
    netstat -natp 结论看不到socket连接
  node02~node03:
    netstat -natp 结论看到很多的socket连接
  node01:
    ipvsadm -lnc    查看偷窥记录本
    pro expire state       source             virtual            destination
    TCP 14:36  ESTABLISHED 192.168.63.1:52944 192.168.63.100:80  192.168.63.4:80
    FIN_WAIT：连接过，偷窥了所有的包
    SYN_RECV：基本上lvs都记录了，证明lvs没事，一定是后边网络层出问题
