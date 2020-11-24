# IOC

## 概念

Ioc 控制反转，是一种设计思想，将原本程序手动创建对象的控制权，交给 spring 框架管理。控制反转是依赖倒置原则的一种思路，实现方法是依赖注入，依赖注入的方式可以使用构造方法或者 setter, 如果某个类有几百个类作为底层，手动注入将会是一个很大的工作量，可以使用 Spring 框架容器实现自动注入。

## 注入过程

1. ApplicationContext context = new ClassPathXmlApplicationContext(...), ApplicationContext 其实就是一个 BeanFactory
2. refresh(): 可以手动调用，需要加锁
3. obtainFreshBeanFactory(): 创建 Bean 容器，加载并注册 Bean
  3.1 new DefaultListableBeanFactory()
  3.2 loadBeanDefinitions(), BeanDefinition 中包含 Bean 的信息
  3.3 解析 xml 配置文件
  3.4 registerBeanDefinition() 中把配置文件中读取到的 BeanDefinition 放到 beanDefinitionMap 中，是一个 ConcurrentHashMap
4. prepareBeanFactory：Spring 把 xml 配置的 bean 都注册以后，会"手动"注册一些特殊的 bean
5. finishBeanFactoryInitialization：利用反射进行创建实例
