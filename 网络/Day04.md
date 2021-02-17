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
