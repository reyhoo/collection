package com.example.administrator.javarxdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/4/7.
 */

public class App  extends Application {

    public static Context instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
