package com.arthurqueiroz.dinamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import com.arthurqueiroz.dinamic.annotation.MyCustomTransaction;

public class MyInvocationHandler implements InvocationHandler {

    private final Object target;
    private final Class<?> targetClass;

    public MyInvocationHandler(Object target) {
        this.target = target;
        this.targetClass = target.getClass();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method targetMethod = getOverriddenMethod(method);

        return getTransactionalMethod(targetMethod)
                    .map(annotation -> handleTransactionalMethod(method, args, annotation))
                    .orElseGet(() -> uncheckedInvoke(method, args));
    }

    private Method getOverriddenMethod(Method method) throws NoSuchMethodException {
        return targetClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
    }

    private Optional<MyCustomTransaction> getTransactionalMethod(Method method) {
        return Optional.ofNullable(method.getDeclaredAnnotation(MyCustomTransaction.class));
    }

    private Object handleTransactionalMethod(Method method, Object[] args, MyCustomTransaction annotation) {
        Object result;
        System.err.println(String.format("Opening transactino [%s] with params %s", 
            annotation.value(), Arrays.toString(args)));
        
        try {
            result = uncheckedInvoke(method, args);
        } catch (RuntimeException e) {
            System.out.println(String.format("Rollback transaction %s...", annotation.value()));
            throw e;
        }

        System.out.println(String.format("Committing transaction %s...", annotation.value()));

        return result;
    }

    private Object uncheckedInvoke(Method method, Object[] args) {
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not invoke method "+ method.getName());
        }
    }
    
}
