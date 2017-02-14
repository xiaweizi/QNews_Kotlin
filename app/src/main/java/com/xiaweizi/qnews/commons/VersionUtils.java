package com.xiaweizi.qnews.commons;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;

import com.blankj.utilcode.utils.AppUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.commons
 * 类名：    VersionUtils
 * 创建者：  夏韦子
 * 创建日期： 2017/2/14
 * 创建时间： 10:50
 */

public class VersionUtils {

    public static void updateVersion(final Context context){

        //获取数据
//        BmobQuery<VersionBean> versionQuery = new BmobQuery<>();
//        versionQuery.getObject("475fbcf700", new QueryListener<VersionBean>() {
//            @Override
//            public void done(final VersionBean versionBean, BmobException e) {
//                if (e == null){
//                    String appVersionName = AppUtils.getAppVersionName(context);
//                    if (!appVersionName.equals((versionBean.getVersionName()))){
//
//                        new AlertDialog.Builder(context)
//                                .setTitle("发现新版本！")
//                                .setMessage("当前版本为:\tversion" + appVersionName + "\n" +
//                                            "最新版本为:\tversion" + versionBean.getVersionName())
//                                .setNegativeButton("取消", null)
//                                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                        if (!NetworkUtils.isAvailableByPing()){
//                                            ToastUtils.showShortToast("当前网络不可用");
//                                            return;
//                                        }
//                                        //版本更新
//                                        versionUpdate(context, versionBean.getDownloadUrl());
//
//                                    }
//                                })
//                                .show();
//
//                    }else {
//                        ToastUtils.showShortToast("当前已经为最新版本");
//                    }
//
//                }else {
//                    ToastUtils.showShortToast("当前已经为最新版本");
//                }
//            }
//        });

    }

    private static void versionUpdate(final Context context, String url){

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setTitle("更新");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();


        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "QNews.apk")
                {

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        dialog.setProgress((int)(progress*100));
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.i("e:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        LogUtils.i("file:" + response.getAbsolutePath());
                        dialog.cancel();

                        AppUtils.installApp(context, response.getAbsolutePath());
                    }
                });
    }
}
