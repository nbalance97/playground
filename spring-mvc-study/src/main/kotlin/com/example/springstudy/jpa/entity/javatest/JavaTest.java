package com.example.springstudy.jpa.entity.javatest;

import org.springframework.util.Assert;

public class JavaTest<T> {

    public boolean isValid(T data) {
        Assert.notNull(data, "으하하하");

        return data.equals("abc");
    }
}
