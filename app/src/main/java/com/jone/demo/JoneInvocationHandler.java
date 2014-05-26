package com.jone.demo;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by jone on 2014/5/8.
 */
public class JoneInvocationHandler implements InvocationHandler {
    private Object object;
    public JoneInvocationHandler(){}
    public JoneInvocationHandler(Object object){
        this.object = object;
    }
    public static Object newInstance(Object object){ //利用代理模式生成Object对象
        return Proxy.newProxyInstance(object.getClass().getClassLoader(),
                object.getClass().getInterfaces(),
                new JoneInvocationHandler(object));
    }

    /**
     * 该方法在被代理执行每个具体类的方法时会自动执行
     * @param o
     * @param method
     * @param objects
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Object resultObj = null;
        long startTime = System.currentTimeMillis();
        resultObj = method.invoke(object, objects);
        long endTime = System.currentTimeMillis();
        System.out.println("JoneInvocationHandler>>" + method.getName() + "方法执行花费: " + (endTime - startTime) + "毫秒.");
        return resultObj;
    }
}
