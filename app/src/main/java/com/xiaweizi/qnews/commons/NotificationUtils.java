package com.xiaweizi.qnews.commons;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xiawei.webviewlib.WebViewActivity;
import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.activity.MainActivity;
import com.xiaweizi.qnews.activity.SplashActivity;
import com.xiaweizi.qnews.bean.MessageBean;

/**
 * 功能描述：    通知栏工具
 * 类名：       com.xiaweizi.qnews.commons.NotificationUtils
 * 创建者：      夏韦子
 * 创建时间：    2017/4/24 13:42
 * 项目名称：    QNews
 */
public class NotificationUtils {
    private Context mContext;
    private NotificationManager mManager;
    public NotificationUtils(Context context){
        this.mContext = context;
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void normalNotify(MessageBean bean){
        Log.i("xwz--->", "NotificationUtils:  " + "normalNotify: " + bean.toString());
        int messageType = bean.type;
        Log.i("xwz--->", "NotificationUtils:  " + "messageType: " + messageType);
        Intent intent = new Intent();
        switch (messageType){
            case 0:
                intent.setClass(mContext, MainActivity.class);
                break;
            case 1:
                intent.setClass(mContext, SplashActivity.class);
                break;
            case 2:
                intent.setClass(mContext, WebViewActivity.class);
                break;

        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setContentTitle("趣闻");
        builder.setContentText(bean.content);
        builder.setContentIntent(contentIntent);

        Notification notification = builder.build();
        notification.icon = R.mipmap.receiver;
        notification.defaults = Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.when = System.currentTimeMillis();
        // 显示在状态栏中的文字
        notification.tickerText = bean.content;

        mManager.notify(0, notification);
    }
}