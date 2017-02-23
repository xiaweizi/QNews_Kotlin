package com.xiaweizi.qnews.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xiaweizi.qnews.bean.JokeBean;
import com.xiaweizi.qnews.commons.Constants;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
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

    private Retrofit createRetrofit() {

        // 给OkHttp添加拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //初始化OkHttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(9, TimeUnit.SECONDS)    //设置连接超时 9s
                .readTimeout(10, TimeUnit.SECONDS)      //设置读取超时 10s
                .addInterceptor(interceptor)            //添加之前创建的拦截器
                .build();

        //返回 Retrofit 对象
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_JOKE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private void getJokeData(){
        Retrofit retrofit = createRetrofit();
        QNewsService qNewsService = retrofit.create(QNewsService.class);
        Observable<JokeBean> jokeBeanObservable = qNewsService.getCurrentJokeData(1, 10);
//        jokeBeanObservable.
    }
}
