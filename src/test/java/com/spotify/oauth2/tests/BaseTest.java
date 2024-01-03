package com.spotify.oauth2.tests;

import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public class BaseTest {
    @BeforeMethod
    public void beforeMethod(Method m){
        System.out.println("Test:" + m.getName());
        System.out.println("Thread:" + Thread.currentThread().getId());
    }
}
