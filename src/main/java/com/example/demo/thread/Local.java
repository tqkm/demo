package com.example.demo.thread;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Local {

    private static boolean s;

    public static int count;

    public static void main(String[] args) {
        Map<Product, String> m = new HashMap<>();
        Product p1 = new Product();
        p1.setIssSt(1);
        Product p2 = new Product();
        p2.setIssSt(2);
        System.out.println(p1.equals(p2));
        m.put(p1, "1");
        m.put(p2, "2");
        p2.setIssSt(1);
        System.out.println(p1.equals(p2));
        System.out.println(m);
        Product p3 = new Product();
        p3.setIssSt(1);
        System.out.println(m.put(p2, "3"));
        System.out.println(m);
    }

    private static void a() throws FileNotFoundException {
        throw new FileNotFoundException();
    }

}
