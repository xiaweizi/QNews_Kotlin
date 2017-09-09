package com.xiaweizi.qnews.activity

import android.app.Application
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.utils.Utils
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.log.LoggerInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * class：   QNews
 * author：  xiaweizi
 * date：    2017/9/9 15:23
 * e-mail:   1012126908@qq.com
 * desc:     Application
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Utils.init(applicationContext)

        JPushInterface.setDebugMode(false)
        JPushInterface.init(this)

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS).build()

        OkHttpUtils.initClient(okHttpClient)
    }
}