package com.dynamic.demo.proxy;

import android.util.Log;

/**
 * Created by Junguo.L on 2020/5/19.
 */
public class ClassImpl implements Class1 {
    @Override
    public void doSomething() {
        Log.e("Tag", "doSomething --- " + getClass().getName());
    }
}
