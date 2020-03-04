package com.example.demo.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Local {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        //ThreadLocal<String> threadLocal = new ThreadLocal<>();
        InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<>();
        threadLocal.set("123");
        for (int i = 0; i < 3; i ++)
            pool.execute(() -> {
                System.out.println(threadLocal.get());
                threadLocal.set(Thread.currentThread().getName());
                System.out.println(threadLocal.get());
            });
        System.out.println(threadLocal.get());
        pool.shutdown();
        var ss = 123;
    }

}
