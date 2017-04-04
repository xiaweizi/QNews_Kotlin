package com.xiaweizi.qnews.commons;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 功能描述：    分享功能
 * 类名：       com.xiaweizi.qnews.commons.ShareUtils
 * 创建者：      夏韦子
 * 创建时间：    2017/4/4 09:10
 * 项目名称：    QNews
 */
public class ShareUtils {

    public static void share(Context context, String shareContent) {
        StringBuffer sb = new StringBuffer();
        sb.append(shareContent);
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "该手机不支持该操作", Toast.LENGTH_LONG).show();
        }
    }

}