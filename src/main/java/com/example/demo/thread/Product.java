package com.example.demo.thread;

import lombok.Data;

import java.util.Objects;

@Data
public class Product implements Comparable<Product>{

    private Integer issSt;//试试
    private String rate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(issSt, product.issSt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issSt);
    }

    @Override
    public int compareTo(Product o) {
        if (this.issSt.equals(o.getIssSt()))
            return -this.getRate().compareTo(o.getRate());
        if (this.issSt.equals(2))
            return 1;
        if (this.issSt.equals(1)) {
            if (o.issSt.equals(5))
                return -1;
            return 1;
        }
        return -1;
    }
}
