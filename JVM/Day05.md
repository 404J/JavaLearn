# Day05

## JVM Runtime Data Area

### Program Counter: JVM 中存放下一条指令的位置（程序计数器），每个线程独有，用于线程切换

### JVM stacks: 每个线程的栈，每个线程独有

- Frame: 栈帧, 每个方法独有

    1. Local variables table: 局部变量表, 非静态方法中, 局部变量表中含有this
    2. Operand stack: 操作数栈
    3. Dynamic linking: 动态链接，比如方法A中执行方法B，方法B通过动态链接找到
    4. Return address: 返回地址，比如方法A中执行方法B，方法B的返回值存放的地方

    ```java
    public class Test {
        public static void main(String[] args) throws Exception {
            /*
            * JVM指令：
            * 1. iconst_1: int 1 进操作数栈
            * 2. istore_1: int 1 出栈并保存到局部变量表中
            */
            int i = 1;

            /*
            * JVM指令：
            * 1. iload_1:     i 从局部变量表拿出并进操作数栈
            * 2. iinc 1 by 1: 局部变量表中的int i 加一
            * 3. istore_1:    int 1 出栈并保存到局部变量表 i 中
            */
            i = i++; // 最终局部变量表中 i 为 1

            /*
            * JVM指令：
            * 1. iinc 1 by 1: 局部变量表中的int i 加一
            * 2. iload_1:     int i 从操作数栈拿出并进操作数栈
            * 3. istore_1:    int 2 出栈并保存到局部变量表 is 中
            */
            i = ++i; // 最终局部变量表 i 为 2

            System.out.println(i);
        }
    }
    ```

- 常见JVM指令

    1. load
    2. store
    3. pop
    4. invokeStatic: 调用静态方法
    5. invokeVirtual: new 的那个对象，就调用该对象的方法。调用多态方法，public方法
    6. invokeSpecial: private 和 构造方法
    7. invokeInterface: List list = new ArrayList(); list.add()
    8. invokeDynamic: lambda 表达式，反射等，动态创建 class

### Native method stacks: 每个线程独有

### Direct memory

### Method area: 线程共享，1.8 之前叫做 PermSpace(设定大小后，不可变，会内存溢出); 1.8 之后叫做 MetaSpace

### Heap: 线程共享
