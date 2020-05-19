package com.dynamic.demo.proxy;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Junguo.L on 2020/5/19.
 */
public class ClassInvokeHandler implements InvocationHandler {


    private final Object target;

    /**
     * 通过构造函数将目标对象注入
     * @param target
     */
    public ClassInvokeHandler(Object target) {
        this.target = target;
    }

    /**
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.e("Tag", "start");
        Object invoke = method.invoke(target, args);
        Log.e("Tag", "end");
        return invoke;
    }
}
