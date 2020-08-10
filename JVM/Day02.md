# Day02

## 类加载

1. Loading: class 文件加载到内存

    - class 文件实际内容（二进制内容）放到内存中，并且生成了一个 Class 的 Object,这个 Object 指向 class 内容的存储地址

    - 不同 class 文件被不同的类加载器加载，双亲委派机制，父子层次（非继承关系）依次是: Bootstrap, Extension, App, Custom ClassLoader

    - 为啥会要双亲委派机制？
    安全考虑，亦防止重复加载

2. Linking

    - Verification: 验证 class 文件格式

    - Preparation: 静态变量赋默认值

    - Resolution: 符号引用指向内存地址

3. Init: 静态变量赋初始值

## 初始化
