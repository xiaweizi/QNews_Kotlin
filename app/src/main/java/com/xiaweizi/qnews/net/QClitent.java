package com.xiaweizi.qnews.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xiaweizi.qnews.BuildConfig;
import com.xiaweizi.qnews.commons.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.net
 * 类名：    QClitent
 * 创建者：  夏韦子
 * 创建日期： 2017/2/23
 * 创建时间： 19:38
 */

public class QClitent {

    private static QClitent mQClient;
    private Retrofit mRetrofit;

    private QClitent(){
        mRetrofit = createRetrofit();
    }

    public static QClitent getInstance() {
        if (mQClient == null){
            synchronized (QClitent.class){
                if (mQClient == null){
                    mQClient = new QClitent();
                }
            }
        }
        return mQClient;
    }

    /**
     * 创建相应的服务接口
     */
    public <T> T create(Class<T> service){
        checkNotNull(service, "service is null");
        return mRetrofit.create(service);
    }

    /**
     * 直接创建 QNewsService
     */
    public QNewsService createQNewsService(){
        return mRetrofit.create(QNewsService.class);
    }


    private  <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    private Retrofit createRetrofit() {
        //初始化OkHttp
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(9, TimeUnit.SECONDS)    //设置连接超时 9s
                .readTimeout(10, TimeUnit.SECONDS);      //设置读取超时 10s

        if (BuildConfig.DEBUG) { // 判断是否为debug
            // 如果为 debug 模式，则添加日志拦截器
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        // 返回 Retrofit 对象
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_JOKE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }
}
