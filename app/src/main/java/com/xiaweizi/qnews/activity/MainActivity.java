package com.xiaweizi.qnews.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.commons.ActivityUtils;
import com.xiaweizi.qnews.fragment.AboutFragment;
import com.xiaweizi.qnews.fragment.JokeFragment;
import com.xiaweizi.qnews.fragment.NewsFragment;
import com.xiaweizi.qnews.fragment.RobotFragment;
import com.xiaweizi.qnews.fragment.TodayFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private NewsFragment newsFragment;
    private JokeFragment jokeFragment;
    private RobotFragment robotFragment;
    private AboutFragment aboutFragment;
    private TodayFragment todayFragment;

    private ActivityUtils utils;
    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        utils = new ActivityUtils(this);

        ImageView iconImage = (ImageView) nvLeft.getHeaderView(0).findViewById(R.id.icon_image);


        Glide.with(this)
                .load("http://img.17gexing.com/uploadfile/2016/07/2/20160725115642623.gif")
                .asGif()
                .centerCrop()
                .into(iconImage);

        /*************************** 第一次进入创建newsFragment ***************************/
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        newsFragment = new NewsFragment();
        transaction.add(R.id.fl_content, newsFragment);
        transaction.commit();

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
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_news:
                        break;
                    case R.id.tab_joke:
                        break;
                    case R.id.tab_robot:
                        break;
                }
            }
        });


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
                    default:
                        break;
                }
                return false;
            }
        });
//        QNewsClient instance = QNewsClient.getInstance();
//        instance.GetNewsData("top", new QNewsCallback() {
//            @Override
//            public void onSuccess(Object response, int id) {
////                Log.i(TAG, "onSuccess: " + response.toString());
//            }
//
//            @Override
//            public void onError(Exception e, int id) {
//
//            }
//        });
//
//        instance.GetQARobotData("你叫什么", new QNewsCallback() {
//            @Override
//            public void onSuccess(Object response, int id) {
////                Log.i(TAG, "onSuccess: " + response.toString());
//            }
//
//            @Override
//            public void onError(Exception e, int id) {
//
//            }
//        });
//
//        instance.GetTodayOfHistoryData(12, 1, new QNewsCallback() {
//            @Override
//            public void onSuccess(Object response, int id) {
////                Log.i(TAG, "onSuccess: " + response.toString());
//            }
//
//            @Override
//            public void onError(Exception e, int id) {
//
//            }
//        });
//
//        instance.GetTodayOfHistoryDetailData(13376, new QNewsCallback() {
//            @Override
//            public void onSuccess(Object response, int id) {
//                TodayOfHistoryDetailBean bean = (TodayOfHistoryDetailBean) response;
//                Log.i(TAG, "GetTodayOfHistoryDetailData: " + bean.toString());
//            }
//
//            @Override
//            public void onError(Exception e, int id) {
//
//            }
//        });

    }

    private void closeDrawerLayout(){
        if (dlActivityMain.isDrawerOpen(Gravity.LEFT)){
            dlActivityMain.closeDrawers();
        }
    }

    private void showNewsDataFragment() {
        hideAllFragment();
        if (newsFragment == null) {
            newsFragment = new NewsFragment();
        } else {
            manager.beginTransaction().show(newsFragment).commit();
        }
    }

    private void showTodayFragment() {
        hideAllFragment();
        if (todayFragment == null) {
            todayFragment = new TodayFragment();
            manager.beginTransaction().add(R.id.fl_content, todayFragment).commit();
        } else {
            manager.beginTransaction().show(todayFragment).commit();
        }
    }

    private void showAboutFragment() {
        hideAllFragment();
        if (aboutFragment == null) {
            aboutFragment = new AboutFragment();
            manager.beginTransaction().add(R.id.fl_content, aboutFragment).commit();
        } else {
            manager.beginTransaction().show(aboutFragment).commit();
        }
    }


    private void showJokeFragment() {
        hideAllFragment();
        if (jokeFragment == null) {
            jokeFragment = new JokeFragment();
            manager.beginTransaction().add(R.id.fl_content, jokeFragment).commit();
        } else {
            manager.beginTransaction().show(jokeFragment).commit();
        }
    }

    private void showRobotFragment() {
        hideAllFragment();
        if (robotFragment == null) {
            robotFragment = new RobotFragment();
            manager.beginTransaction().add(R.id.fl_content, robotFragment).commit();
        } else {
            manager.beginTransaction().show(robotFragment).commit();
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
        hideTransaction.commit();
    }


    long lastTime = 0;

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        if ((curTime - lastTime) > 2000) {
            utils.showToast("再按一次退出应用");
            lastTime = curTime;
        } else {
            finish();
        }
    }
}
