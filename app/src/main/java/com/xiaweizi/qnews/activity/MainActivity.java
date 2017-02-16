package com.xiaweizi.qnews.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.SPUtils;
import com.bumptech.glide.Glide;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.commons.ActivityUtils;
import com.xiaweizi.qnews.commons.VersionUtils;
import com.xiaweizi.qnews.fragment.AboutFragment;
import com.xiaweizi.qnews.fragment.GIFFragment;
import com.xiaweizi.qnews.fragment.JokeFragment;
import com.xiaweizi.qnews.fragment.NewsFragment;
import com.xiaweizi.qnews.fragment.RobotFragment;
import com.xiaweizi.qnews.fragment.TodayFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity------>";
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.nv_left)
    NavigationView nvLeft;
    @BindView(R.id.dl_activity_main)
    DrawerLayout dlActivityMain;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    private NewsFragment newsFragment;      //新闻数据
    private JokeFragment jokeFragment;      //段子
    private RobotFragment robotFragment;    //机器人
    private AboutFragment aboutFragment;    //关于
    private TodayFragment todayFragment;    //历史上的今天
    private GIFFragment gifFragment;        //动态图

    private ActivityUtils utils;
    private BottomBar bottomBar;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SPUtils util = new SPUtils("theme_id");
        int theme_id = util.getInt("theme_id", R.style.AppTheme);
        setTheme(theme_id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        //设置软键盘的模式为适应屏幕模式
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        utils = new ActivityUtils(this);


//
//        QNewsClient.getInstance().GetWeatherDetailData("南京", new QNewsCallback<WeatherDetailBean>() {
//            @Override
//            public void onSuccess(WeatherDetailBean response, int id) {
//                WeatherDetailBean.ResultBean.DataBean.RealtimeBean realtime = response.getResult().getData().getRealtime();
//
//                String date = realtime.getDate();   //日期
//                String time = realtime.getTime();   //更新时间
//                int week = realtime.getWeek();      //星期
//                String moon = realtime.getMoon();   //阴历
//                String temperature = realtime.getWeather().getTemperature();  //温度
//                String info = realtime.getWeather().getInfo();                //天气状况
//                String direct = realtime.getWind().getDirect();               //风向
//                String power = realtime.getWind().getPower();                 //风向级别
//            }
//
//            @Override
//            public void onError(Exception e, int id) {
//
//            }
//        });

        /*************************** 左侧 侧滑菜单 设置头像图片 ***************************/
        ImageView iconImage = (ImageView) nvLeft.getHeaderView(0).findViewById(R.id.icon_image);
        final ImageView ivBmp = (ImageView) nvLeft.getHeaderView(0).findViewById(R.id.iv_head_bg);
        Glide.with(this)
                .load("http://img.17gexing.com/uploadfile/2016/07/2/20160725115642623.gif")
                .asGif()
                .centerCrop()
                .into(iconImage);

        OkHttpUtils.get().url("http://guolin.tech/api/bing_pic").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Glide.with(MainActivity.this)
                        .load(response)
                        .crossFade()
                        .centerCrop()
                        .into(ivBmp);
            }
        });

        iconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomBar.selectTabAtPosition(4, true);
            }
        });

        /*************************** 第一次进入创建newsFragment ***************************/
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        newsFragment = new NewsFragment();
        transaction.add(R.id.fl_content, newsFragment, "news");
        transaction.commit();

        /*************************** 底部bar 设置点击事件 ***************************/
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_news:
                        showNewsDataFragment();
                        nvLeft.setCheckedItem(R.id.nav_news);
                        closeDrawerLayout();
                        break;
                    case R.id.tab_joke:
                        nvLeft.setCheckedItem(R.id.nav_duanzi);
                        showJokeFragment();
                        closeDrawerLayout();
                        break;
                    case R.id.tab_today:
                        nvLeft.setCheckedItem(R.id.nav_today_of_history);
                        showTodayFragment();
                        closeDrawerLayout();
                        break;
                    case R.id.tab_robot:
                        nvLeft.setCheckedItem(R.id.nav_robot);
                        showRobotFragment();
                        closeDrawerLayout();
                        break;
                    case R.id.tab_about:
                        nvLeft.setCheckedItem(R.id.nav_other);
                        showAboutFragment();
                        closeDrawerLayout();
                        break;
                }
            }
        });
        //底部bar设置再次点击事件
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_news:
                        break;
                    case R.id.tab_joke:
                        showGifFragment();
                        break;
                    case R.id.tab_robot:
                        break;
                }
            }
        });


        /*************************** 左侧 侧滑菜单 设置选择事件 ***************************/
        nvLeft.setCheckedItem(R.id.nav_news);
        nvLeft.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                nvLeft.setCheckedItem(item.getItemId());
                dlActivityMain.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_news:
                        bottomBar.selectTabAtPosition(0, true);
                        break;
                    case R.id.nav_duanzi:
                        bottomBar.selectTabAtPosition(1, true);
                        break;
                    case R.id.nav_today_of_history:
                        bottomBar.selectTabAtPosition(2, true);
                        break;
                    case R.id.nav_robot:
                        bottomBar.selectTabAtPosition(3, true);
                        break;
                    case R.id.nav_other:
                        bottomBar.selectTabAtPosition(4, true);
                        break;
                    case R.id.nav_clear_cache:
                        clearCache();
                        break;
                    case R.id.nav_version_update:
                        VersionUtils.updateVersion(MainActivity.this);
                        break;
                    case R.id.nav_change_theme:
                        alertChangeTheme();
                        break;
                    case R.id.nav_day_night:
                        changeTheme(9);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }


    /**
     * 关闭左侧 侧滑菜单
     */
    private void closeDrawerLayout() {
        if (dlActivityMain.isDrawerOpen(Gravity.LEFT)) {
            dlActivityMain.closeDrawers();
        }
    }

    /**
     * 展示 新闻数据 Fragment
     */
    private void showNewsDataFragment() {
        hideAllFragment();
        if (newsFragment == null) {
            newsFragment = new NewsFragment();
        } else {
            manager.beginTransaction().show(newsFragment).commit();
        }
    }

    /**
     * 展示 历史上的今天 Fragment
     */
    private void showTodayFragment() {
        hideAllFragment();
        if (todayFragment == null) {
            todayFragment = new TodayFragment();
            manager.beginTransaction().add(R.id.fl_content, todayFragment).commit();
        } else {
            manager.beginTransaction().show(todayFragment).commit();
        }
    }

    /**
     * 展示 关于 Fragment
     */
    private void showAboutFragment() {
        hideAllFragment();
        if (aboutFragment == null) {
            aboutFragment = new AboutFragment();
            manager.beginTransaction().add(R.id.fl_content, aboutFragment).commit();
        } else {
            manager.beginTransaction().show(aboutFragment).commit();
        }
    }


    /**
     * 展示 段子 Fragment
     */
    private void showJokeFragment() {
        hideAllFragment();
        if (jokeFragment == null) {
            jokeFragment = new JokeFragment();
            manager.beginTransaction().add(R.id.fl_content, jokeFragment).commit();
        } else {
            manager.beginTransaction().show(jokeFragment).commit();
        }
    }

    /**
     * 展示 图铃机器人 Fragment
     */
    private void showRobotFragment() {
        hideAllFragment();
        if (robotFragment == null) {
            robotFragment = new RobotFragment();
            manager.beginTransaction().add(R.id.fl_content, robotFragment).commit();
        } else {
            manager.beginTransaction().show(robotFragment).commit();
        }
    }

    private void showGifFragment() {
        hideAllFragment();
        if (gifFragment == null) {
            gifFragment = new GIFFragment();
            manager.beginTransaction().add(R.id.fl_content, gifFragment).commit();
        } else {
            manager.beginTransaction().show(gifFragment).commit();
        }
    }

    /**
     * 隐藏所有的fragment
     */
    private void hideAllFragment() {
        FragmentTransaction hideTransaction = manager.beginTransaction();
        if (newsFragment != null) {
            hideTransaction.hide(newsFragment);
        }
        if (jokeFragment != null) {
            hideTransaction.hide(jokeFragment);
        }
        if (robotFragment != null) {
            hideTransaction.hide(robotFragment);
        }
        if (aboutFragment != null) {
            hideTransaction.hide(aboutFragment);
        }
        if (todayFragment != null) {
            hideTransaction.hide(todayFragment);
        }
        if (gifFragment != null) {
            hideTransaction.hide(gifFragment);
        }
        hideTransaction.commit();
    }


    long lastTime = 0;

    /**
     * 2秒内连续点击 back 键，退出应用
     */
    @Override
    public void onBackPressed() {
        if (dlActivityMain.isDrawerOpen(Gravity.LEFT)) {
            dlActivityMain.closeDrawers();
            return;
        }
        long curTime = System.currentTimeMillis();
        if ((curTime - lastTime) > 2000) {
            utils.showToast("再按一次退出应用");
            lastTime = curTime;
        } else {
            finish();
        }
    }


    private void clearCache() {
        String dirSize = FileUtils.getDirSize(getCacheDir());
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("确定要清理缓存")
                .setMessage("缓存大小：" + dirSize)
                .setPositiveButton("清理", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FileUtils.deleteDir(getCacheDir());
                        utils.showToast("清理成功");
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void changeTheme(int index) {
        int[] themes = new int[]{R.style.AppTheme, R.style.AppTheme_Blue, R.style.AppTheme_Green,
                R.style.AppTheme_Orange, R.style.AppTheme_Pink, R.style.AppTheme_Sky,
                R.style.AppTheme_Purple, R.style.AppTheme_PP, R.style.AppTheme_Yellow, R.style.AppTheme_Night};
        SPUtils utils = new SPUtils("theme_id");
        utils.putInt("theme_id", themes[index]);
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }


    /**
     * 动态换肤
     */
    private void alertChangeTheme() {
        View view = View.inflate(this, R.layout.item_change_theme, null);
        builder = new AlertDialog.Builder(this)
                .setView(view);
        builder.show();
        view.findViewById(R.id.tv_red).setOnClickListener(listener);
        view.findViewById(R.id.tv_green).setOnClickListener(listener);
        view.findViewById(R.id.tv_blue).setOnClickListener(listener);
        view.findViewById(R.id.tv_orange).setOnClickListener(listener);
        view.findViewById(R.id.tv_pink).setOnClickListener(listener);
        view.findViewById(R.id.tv_sky).setOnClickListener(listener);
        view.findViewById(R.id.tv_purple).setOnClickListener(listener);
        view.findViewById(R.id.tv_pp).setOnClickListener(listener);
        view.findViewById(R.id.tv_yellow).setOnClickListener(listener);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_red:
                    changeTheme(0);
                    break;
                case R.id.tv_blue:
                    changeTheme(1);
                    break;
                case R.id.tv_green:
                    changeTheme(2);
                    break;
                case R.id.tv_orange:
                    changeTheme(3);
                    break;
                case R.id.tv_pink:
                    changeTheme(4);
                    break;
                case R.id.tv_sky:
                    changeTheme(5);
                    break;
                case R.id.tv_purple:
                    changeTheme(6);
                    break;
                case R.id.tv_pp:
                    changeTheme(7);
                    break;
                case R.id.tv_yellow:
                    changeTheme(8);
                    break;


                default:
                    break;
            }
        }
    };

}
