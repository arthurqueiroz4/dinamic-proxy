package com.arthurqueiroz.dinamic.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import com.arthurqueiroz.ProxyApp;
import com.arthurqueiroz.dinamic.proxy.annotations.TransactionalService;

@SuppressWarnings("unused")
public class ProxyFactory {
    
    public ProxyFactory(Package packageLookUp) {
        Reflections reflections = new Reflections(packageLookUp);
        Set<Class<?>> transactionalServiceClasses = reflections.getTypesAnnotatedWith(TransactionalService.class);
    }

    @SuppressWarnings("deprecation")
    private Object instantiateClass(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Could not instantiate class " + clazz.getName());
        }
    }

    private MyCustomProxy createProxy(Object bean) {
        InvocationHandler handler = new MyInvocationHandler(bean);
        Object proxyObj = Proxy.newProxyInstance(
            ProxyApp.class.getClassLoader(),
            bean.getClass().getInterfaces(),
            handler);

        return new MyCustomProxy(Arrays.asList(bean.getClass().getInterfaces()), proxyObj);
    }
}
