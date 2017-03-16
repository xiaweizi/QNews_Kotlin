package com.xiaweizi.qnews.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.utils.SPUtils;
import com.xiaweizi.qnews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity ---->";

    @BindView (R.id.iv_splash)
    ImageView      ivSplash;
    @BindView (R.id.activity_splash)
    RelativeLayout activitySplash;
    private AnimatorSet set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SPUtils util     = new SPUtils("theme_id");
        int     theme_id = util.getInt("theme_id", R.style.AppTheme);
        setTheme(theme_id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        set = new AnimatorSet();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(ivSplash, "translationX", 600, 0);
        ObjectAnimator translationY = ObjectAnimator
                .ofFloat(ivSplash, "translationY", -100, 90, -80, 70, -60, 50);

        set.playTogether(translationX, translationY);
        set.setDuration(2000);
        addListener();

        /*************************** 版本更新校验 ***************************/
        //        //获取数据
        //        BmobQuery<VersionBean> versionQuery = new BmobQuery<>();
        //        versionQuery.getObject("475fbcf700", new QueryListener<VersionBean>() {
        //            @Override
        //            public void done(final VersionBean versionBean, BmobException e) {
        //                if (e == null){
        //                    LogUtils.i("versionBean" + versionBean.toString());
        //                    String appVersionName = AppUtils.getAppVersionName(SplashActivity.this);
        //                    if (!appVersionName.equals((versionBean.getVersionName()))){
        //
        //                        new AlertDialog.Builder(SplashActivity.this)
        //                                .setTitle("发现新版本！")
        //                                .setMessage("当前版本为:\t\tversion" + appVersionName + "\n" +
        //                                        "最新版本为:\t\tversion" + versionBean.getVersionName())
        //                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
        //                                    @Override
        //                                    public void onClick(DialogInterface dialog, int which) {
        //                                        addListener();
        //                                    }
        //                                })
        //                                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
        //                                    @Override
        //                                    public void onClick(DialogInterface dialog, int which) {
        //                                        LogUtils.i("更新 + " + versionBean.getDownloadUrl());
        //                                        addListener();
        //                                    }
        //                                })
        //                                .show();
        //
        //                    }else {
        //                        addListener();
        //                    }
        //
        //                }else {
        //                    addListener();
        //                }
        //            }
        //        });


    }

    private void addListener() {
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                try {
                    Thread.sleep(500);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
