package com.dynamic.demo;

import android.app.ActivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dynamic.demo.proxy.Class1;
import com.dynamic.demo.proxy.ClassImpl;
import com.dynamic.demo.proxy.ClassInvokeHandler;
import com.dynamic.demo.reflect.ClassB2Mock;
import com.dynamic.demo.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import androidx.appcompat.app.AppCompatActivity;
import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    private Dynamic dynamic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void load(View view) {
//        loadDexClass();

        reflect();
    }

    private void reflect() {
//
//        //测试Singleton
//        AMN.getDefault().doSomething();
//        test5();
//        AMN.getDefault().doSomething();

        // 代理模式
        Class1 aClass = new ClassImpl();
        /**
         * @param   loader 目标对象对应的ClassLoader
         * @param   interfaces 目标对象所实现的接口类型，这里是Class1
         * @param   h 实现了InvocationHandler 接口的类对象，通过它的构造函数将目标对象注入
         *
         * @return 返回的是实现了Class1接口的对象：Class1Proxy 代理对象
         *
         * 执行Class1Proxy（newProxyInstance的返回值）的doSomething方法，其实就是在执行ClassInvokeHandler类的invoke方法。
         * 这个invoke方法就是代理模式的设计思想。它有一个method参数，执行method的invoke方法，就是在执行Class1的doSomething方法
         *
         * Proxy.newProxyInstance 方法可以用在任何一个接口类型的对象上，为这个对象增加新功能，所以称为"动态代理"。
         * 在插件化领域，Proxy.newProxyInstance生成的对象，直接替换掉原来的对象，这个技术就是 Hook 技术。
         */
        Class1 c = (Class1) Proxy.newProxyInstance(aClass.getClass().getClassLoader(),
                aClass.getClass().getInterfaces(),
                new ClassInvokeHandler(aClass));
        c.doSomething();
    }

    public void test5() {
        ActivityManager am;
        try {
            // gDefault是一个 android.util.Singleton对象; 我们取出这个单例里面的字段
            Class<?> singleton = Class.forName("jianqiang.com.testreflection.Singleton");
            Field mInstanceField = singleton.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);

            //获取AMN的gDefault单例gDefault，gDefault是静态的
            Class<?> activityManagerNativeClass = Class.forName("jianqiang.com.testreflection.AMN");
            Field gDefaultField = activityManagerNativeClass.getDeclaredField("gDefault");
            gDefaultField.setAccessible(true);
            Object gDefault = gDefaultField.get(null);

            // AMN的gDefault对象里面原始的 B2对象
            Object rawB2Object = mInstanceField.get(gDefault);

            // 创建一个这个对象的代理对象ClassB2Mock, 然后替换这个字段, 让我们的代理对象帮忙干活
            Class<?> classB2Interface = Class.forName("jianqiang.com.testreflection.ClassB2Interface");
            Object proxy = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class<?>[]{classB2Interface},
                    new ClassB2Mock(rawB2Object));
            mInstanceField.set(gDefault, proxy);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadDexClass() {
        File cacheFile = FileUtil.getCacheDir(getApplicationContext());

        String internalPath = cacheFile.getAbsolutePath() + File.separator + "dynamic_dex.jar";

        Log.e("Tag", "internalPath = " + internalPath + "    cacheFilePath = " + cacheFile.getAbsolutePath());
        File desFile = new File(internalPath);

        try {

            if (!desFile.exists()) {

                desFile.createNewFile();

                // 将assets下的dex文件复制到目标位置
                FileUtil.copyFiles(this, "dynamic_dex.jar", desFile);
            }

        } catch (IOException e) {

            e.printStackTrace();
            Log.e("Tag", "File Exception = " + e.getLocalizedMessage());
        }

        //下面开始加载dex class
        DexClassLoader dexClassLoader = new DexClassLoader(internalPath, cacheFile.getAbsolutePath(), null, getClassLoader());

        try {

            //加载的类名为jar文件里面完整类名，写错会找不到此类hh
            Class libClazz = dexClassLoader.loadClass("com.dynamic.demo.dynamic.DynamicImpl");

            dynamic = (Dynamic) libClazz.newInstance();

            Toast.makeText(this, dynamic.sayHell(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            e.printStackTrace();
            Log.e("Tag", "dex Exception = " + e.getLocalizedMessage());
        }

    }
}
