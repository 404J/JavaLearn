# Day02

1. Loading: class 文件加载到内存

    - class 文件实际内容（二进制内容）放到内存中，并且生成了一个 Class 的 Object,这个 Object 指向 class 内容的存储地址

    - 不同 class 文件被不同的类加载器加载，双亲委派机制(ClassLoader::loadClass 源码)，父子层次（非继承关系）依次是: Bootstrap, Extension, App, Custom ClassLoader

    - 为啥会要双亲委派机制？
    安全考虑，亦防止重复加载

    - 自定义类加载器 (设计模式：模板方法)
    继承 CalssLoader, 实现 findClass 方法, 使用方法 defineClass

    ```java
    code
    ```

2. Linking

    - Verification: 验证 class 文件格式

    - Preparation: 静态变量赋默认值

    - Resolution: 符号引用指向内存地址

3. Initializing: 静态变量赋初始值

4. 其他知识：jVM 中 java 执行方式：解释执行 + 热点代码编译执行
