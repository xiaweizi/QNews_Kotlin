package com.xiaweizi.qnews.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import com.blankj.utilcode.utils.LogUtils;
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

    private Fragment currentFragment;

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
        }
        //设置软键盘的模式为适应屏幕模式
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_main);
        retrieveFragment();
        ButterKnife.bind(this);

        utils = new ActivityUtils(this);


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

        /*************************** 底部bar 设置点击事件 ***************************/
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_news:
                        LogUtils.i("setOnTabSelectListener");
                        if (newsFragment == null) newsFragment = new NewsFragment();
                        switchFragment(newsFragment);
                        nvLeft.setCheckedItem(R.id.nav_news);
                        closeDrawerLayout();
                        break;
                    case R.id.tab_joke:
                        nvLeft.setCheckedItem(R.id.nav_duanzi);
                        if (jokeFragment == null) jokeFragment = new JokeFragment();
                        switchFragment(jokeFragment);
                        closeDrawerLayout();
                        break;
                    case R.id.tab_today:
                        nvLeft.setCheckedItem(R.id.nav_today_of_history);
                        if (todayFragment == null) todayFragment = new TodayFragment();
                        switchFragment(todayFragment);
                        closeDrawerLayout();
                        break;
                    case R.id.tab_robot:
                        nvLeft.setCheckedItem(R.id.nav_robot);
                        if (robotFragment == null) robotFragment = new RobotFragment();
                        switchFragment(robotFragment);
                        closeDrawerLayout();
                        break;
                    case R.id.tab_about:
                        nvLeft.setCheckedItem(R.id.nav_other);
                        if (aboutFragment == null) aboutFragment = new AboutFragment();
                        switchFragment(aboutFragment);
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
                        if (gifFragment == null) gifFragment = new GIFFragment();
                        switchFragment(gifFragment);
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
     * 切换Fragment的显示
     *
     * @param target 要切换的 Fragment
     */
    private void switchFragment(Fragment target) {

        // 如果当前的fragment 就是要替换的fragment 就直接return
        if (currentFragment == target) return;

        // 获得 Fragment 事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // 如果当前Fragment不为空，则隐藏当前的Fragment
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }

        // 如果要显示的Fragment 已经添加了，那么直接 show
        if (target.isAdded()) {
            transaction.show(target);
        } else {
            // 如果要显示的Fragment没有添加，就添加进去
            transaction.add(R.id.fl_content, target, target.getClass().getName());
        }

        // 事务进行提交
        transaction.commit();

        //并将要显示的Fragment 设为当前的 Fragment
        currentFragment = target;
    }

    /**
     * 找回FragmentManager中存储的Fragment
     */
    private void retrieveFragment() {
        FragmentManager manager = getSupportFragmentManager();
        newsFragment = (NewsFragment) manager.findFragmentByTag("NewsFragment");
        jokeFragment = (JokeFragment) manager.findFragmentByTag("JokeFragment");
        todayFragment = (TodayFragment) manager.findFragmentByTag("TodayFragment");
        robotFragment = (RobotFragment) manager.findFragmentByTag("RobotFragment");
        aboutFragment = (AboutFragment) manager.findFragmentByTag("AboutFragment");
        gifFragment = (GIFFragment) manager.findFragmentByTag("GifFragment");
    }


    /**
     * 关闭左侧 侧滑菜单
     */
    private void closeDrawerLayout() {
        if (dlActivityMain.isDrawerOpen(Gravity.LEFT)) {
            dlActivityMain.closeDrawers();
        }
    }

    long lastTime = 0;

    /**
     * 2秒内连续点击 back 键，退出应用
     */
    @Override
    public void onBackPressed() {
        // 判断侧滑菜单是否开启，如果开启则先关闭
        if (dlActivityMain.isDrawerOpen(Gravity.LEFT)) {
            dlActivityMain.closeDrawers();
            return;
        }
        // 判断当前fragment 是不是 新闻fragment，如果不是先退到新闻fragment
        if (currentFragment != newsFragment){
            bottomBar.selectTabAtPosition(0);
            return;
        }

        long curTime = System.currentTimeMillis();
        if ((curTime - lastTime) > 2000) {
            utils.showToast("再按一次退出应用");
            lastTime = curTime;
        } else {
            moveTaskToBack(true);
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
