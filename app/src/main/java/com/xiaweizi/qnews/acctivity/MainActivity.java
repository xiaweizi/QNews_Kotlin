package com.xiaweizi.qnews.acctivity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.bean.TodayOfHistoryDetailBean;
import com.xiaweizi.qnews.net.QNewsCallback;
import com.xiaweizi.qnews.net.QNewsClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity------>";
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.main_viewpager)
    ViewPager mainViewpager;
    private String[] types;

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

        QNewsClient instance = QNewsClient.getInstance();
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

        instance.GetTodayOfHistoryDetailData(13376, new QNewsCallback() {
            @Override
            public void onSuccess(Object response, int id) {
                TodayOfHistoryDetailBean bean = (TodayOfHistoryDetailBean) response;
                Log.i(TAG, "GetTodayOfHistoryDetailData: " + bean.toString());
            }

            @Override
            public void onError(Exception e, int id) {

            }
        });

        types = getResources().getStringArray(R.array.news_type_cn);
        Log.i(TAG, "onCreate: "+ types[0]);
        mainViewpager.setAdapter(new MyAdapter());
        tabLayout.setupWithViewPager(mainViewpager);


    }

    private class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return types.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return types[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView view = new TextView(MainActivity.this);
            view.setText("第"+position+"页");
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
