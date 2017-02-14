package com.xiaweizi.qnews.activity;

import android.app.Application;

import com.blankj.utilcode.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews
 * 类名：    MyApplication
 * 创建者：  夏韦子
 * 创建日期： 2017/2/9
 * 创建时间： 9:55
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(getApplicationContext());
//        Bmob.initialize(this, "5597c24c18ec706d100033f915b79153");


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
}
