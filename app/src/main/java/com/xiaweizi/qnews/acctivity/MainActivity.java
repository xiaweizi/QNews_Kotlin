package com.xiaweizi.qnews.acctivity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.fragment.NewsFragment;
import com.xiaweizi.qnews.net.QNewsClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity------>";
    @BindView(R.id.fl_news_data)
    FrameLayout flNewsData;
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

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fl_news_data, new NewsFragment()).commit();

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


}
