package com.J404.Test;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello GC");
        List list = new ArrayList();
        for (;;) {
            list.add(new byte[1024 * 1024]);
        }
    }
}

