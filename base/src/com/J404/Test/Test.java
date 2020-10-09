package com.J404.Test;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {
        List list = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            User user = m();
            int a = user.age;
            list.add(a);
        }
        System.out.println("end");
        TimeUnit.sleep(1000);
    }
    static User m() {
        User user = new User();
        return user;
    }
}

class User {
    int age = 0;
}

