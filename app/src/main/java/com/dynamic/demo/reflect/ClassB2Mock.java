package com.dynamic.demo.reflect;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Junguo.L on 2020/5/18.
 */
public class ClassB2Mock implements InvocationHandler {
    Object mBase;

    public ClassB2Mock(Object base) {
        mBase = base;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        Log.e("bao", method.getName());

        if ("doSomething".equals(method.getName())) {
            print();
        }

        return method.invoke(mBase, objects);
    }

    void print() {
        Log.v("baobao", "hello");
    }
}
