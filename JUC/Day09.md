# Day09

## Disruptor

- 存储方式
  环形 Buffer 队列，使用数组实现，采用覆盖旧元素，不用维护首尾指针，维护一个指向下一个可用位置的 sequence，sequence = num & (size - 1)

