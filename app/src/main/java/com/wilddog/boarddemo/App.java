package com.wilddog.boarddemo;

import android.app.Application;

import com.wilddog.boarddemo.util.Constants;
import com.wilddog.toolbar.util.QiniuUtil;
import com.wilddog.wilddogcore.WilddogApp;
import com.wilddog.wilddogcore.WilddogOptions;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        WilddogOptions.Builder builder = new WilddogOptions.Builder().setSyncUrl("http://" + Constants.APPID + ".wilddogio.com");
        WilddogOptions options = builder.build();
        WilddogApp.initializeApp(this, options);
        QiniuUtil.getInstance().init();
    }
}
