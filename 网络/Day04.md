# Day04

## LVS 和 RS 单点故障

### 解决单点故障的思路

- 主备
- 主主

### keepalived

解决 LVS/RS 的单点故障，实现 HA

- 监控自己的服务
- Master 向外广播自己的状态
- Backup 监听 Master 的状态，Master 宕机之后进行选举
- 配置 VIP，添加 LVS 的配置
- 对 Real Server 进行健康检查，更新 LVS 的配置

## LVS DR Keepalived 模型 负载均衡器 搭建

node01,node04:
  yum install keepalived ipvsadm -y
  配置：
    cd /etc/keepalived/
    cp keepalived.conf keepalived.conf.bak
    vi keepalived.conf
      node01:
        vrrp_instance VI_1 {
          state MASTER         //  node04  BACKUP
          interface ens33
          virtual_router_id 51
          priority 100     //   node04   50
          advert_int 1
          authentication {
            auth_type PASS
            auth_pass 1111
          }
          virtual_ipaddress {
            192.168.63.100/24 dev ens33 label ens33:3
          }
        }
        virtual_server 192.168.63.100 80 {
          delay_loop 6
          lb_algo rr
          lb_kind DR
          nat_mask 255.255.255.0
          persistence_timeout 0
          protocol TCP  

          real_server 192.168.63.4 80 {
            weight 1
            HTTP_GET {
              url {
                path /
                status_code 200
              }
              connect_timeout 3
              nb_get_retry 3
              delay_before_retry 3
            }   
          }       
          real_server 192.168.63.5 80 {
            weight 1
            HTTP_GET {
              url {
                path /
                status_code 200
              }
              connect_timeout 3
              nb_get_retry 3
              delay_before_retry 3
            }
          }
        }
      scp  ./keepalived.conf  root@node04:`pwd`

> 主要防火墙的问题！！！

## keepalived 对比 zookeeper 进行 HA

keepalive 是一个程序，如果异常退出会导致 LVS 出现问题，zookeeper 则可以使用集群实现服务治理
