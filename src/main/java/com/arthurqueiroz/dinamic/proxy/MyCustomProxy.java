package com.arthurqueiroz.dinamic.proxy;

import java.util.List;

public class MyCustomProxy {
    private List<Class<?>> interfaces;
    private Object proxy;

    public MyCustomProxy(List<Class<?>> interfaces, Object proxy) {
        this.interfaces = interfaces;
        this.proxy = proxy;
    }

    public Object getJdkProxy() {
        return proxy;
    }

    public boolean hasInterface(Class<?> interfac) {
        return interfaces.contains(interfac);
    }
}
