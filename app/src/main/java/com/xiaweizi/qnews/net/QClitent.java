package com.xiaweizi.qnews.net;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xiaweizi.qnews.BuildConfig;
import com.xiaweizi.qnews.bean.JokeBean;
import com.xiaweizi.qnews.commons.Constants;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
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

    private static final String TAG = "QClitent---->";

    private static <T> T createRetrofit(Class T) {

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_JOKE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return (T) retrofit.create(T);
    }

    public static void getJokeData() {
        QNewsService service = createRetrofit(QNewsService.class);
        service.getCurrentJokeData(1,5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JokeBean>() {
                    @Override
                    public void accept(JokeBean jokeBean) throws Exception {
                        Log.i(TAG, "accept: " + jokeBean.getResult().getData().get(0).getContent());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "accept: " + throwable.getMessage());
                    }
                });
    }
}
