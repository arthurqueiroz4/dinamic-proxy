package com.arthurqueiroz.dinamic.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler {

    private Object target;

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = System.nanoTime();
        Object result = method.invoke(target, args);

        System.out.println(method.getName() + "executed in " + (System.nanoTime() - startTime) + " nanoseconds");
        return result;
    }
    
}
