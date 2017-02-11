package com.xiaweizi.qnews.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.fragment.JokeFragment;
import com.xiaweizi.qnews.fragment.NewsFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /*************************** 第一次进入创建newsFragment ***************************/
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        newsFragment = new NewsFragment();
        transaction.add(R.id.fl_content, newsFragment);
        transaction.commit();

        nvLeft.setCheckedItem(R.id.nav_news);
        nvLeft.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                nvLeft.setCheckedItem(item.getItemId());
                dlActivityMain.closeDrawers();
                switch (item.getItemId()){
                    case R.id.nav_news:
                        hideAllFragment();
                        showNewsDataFragment();
                        break;
                    case R.id.nav_duanzi:
                        hideAllFragment();
                        showJokeFragment();
                        break;
                    case R.id.nav_today_of_history:

                        break;
                    case R.id.nav_robot:

                        break;
                    case R.id.nav_other:

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

    private void showNewsDataFragment(){
        if (newsFragment == null){
            newsFragment = new NewsFragment();
        }else {
            manager.beginTransaction().show(newsFragment).commit();
        }
    }

    /**
     * 隐藏所有的fragment
     */
    private void showJokeFragment(){
        if (jokeFragment == null){
            jokeFragment = new JokeFragment();
            manager.beginTransaction().add(R.id.fl_content, jokeFragment).commit();
        }else {
            manager.beginTransaction().show(jokeFragment).commit();
        }
    }

    private void hideAllFragment(){
        FragmentTransaction hideTransaction = manager.beginTransaction();
        if (newsFragment != null){
            hideTransaction.hide(newsFragment);
        }
        if (jokeFragment != null){
            hideTransaction.hide(jokeFragment);
        }
        hideTransaction.commit();
    }

}
