package com.dynamic.demo.reflect;

/**
 * Created by Junguo.L on 2020/5/18.
 *
 * @hide
 */
public abstract class Singleton<T> {

    private T mInstance;

    protected abstract T create();

    public final T get() {
        synchronized (this) {
            if (mInstance == null) {
                mInstance = create();
            }
            return mInstance;
        }
    }
}
