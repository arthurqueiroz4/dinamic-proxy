package com.arthurqueiroz.dinamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import com.arthurqueiroz.ProxyApp;
import com.arthurqueiroz.dinamic.annotation.TransactionalService;

public class ProxyFactory {

    private List<MyCustomProxy> beansRegistry;
    
    public ProxyFactory(Package packageLookUp) {

        Reflections reflections = new Reflections(
            new ConfigurationBuilder()
                .forPackages(packageLookUp.getName())
        );
        Set<Class<?>> transactionalServiceClasses = reflections.getTypesAnnotatedWith(TransactionalService.class);

        List<?> beans = instantiateBeans(transactionalServiceClasses);
        beansRegistry = createProxies(beans);
    }

    public <T> T getBean(Class<T> clazz) {
        Object proxy = beansRegistry.stream()
                .filter(p -> p.hasInterface(clazz))
                .findFirst()
                .map(MyCustomProxy::getJdkProxy)
                .orElseThrow(() -> new RuntimeException("No Bean found for class " + clazz));
        return clazz.cast(proxy);
    }

    private List<?> instantiateBeans(Set<Class<?>> annotated) {
        return annotated.stream()
                .map(this::instantiateClass)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("deprecation")
    private Object instantiateClass(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Could not instantiate class " + clazz.getName());
        }
    }

    private List<MyCustomProxy> createProxies(List<?> beans) {
        return beans.stream()
                .map(this::createProxy)
                .collect(Collectors.toList());
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
