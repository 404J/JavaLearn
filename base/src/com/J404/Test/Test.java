package com.J404.Test;

import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) throws Exception {
        Class clazz = Class.forName("com.J404.Hello");
        Method method = clazz.getMethod("m");
        method.invoke(clazz.getConstructor().newInstance());
    }
}

