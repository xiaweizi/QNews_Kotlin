package com.xiaweizi.qnews.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.SPUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.bean.TodayOfHistoryDetailBean;
import com.xiaweizi.qnews.commons.Constants;
import com.xiaweizi.qnews.commons.LogUtils;
import com.xiaweizi.qnews.net.QClitent;
import com.xiaweizi.qnews.net.QNewsService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 历史上的今天详情界面
 */
public class TodayDetailActivity extends AppCompatActivity {


    @BindView(R.id.tb_today)
    Toolbar tbToday;
    @BindView(R.id.collapsing_toolbar_today)
    CollapsingToolbarLayout collapsingToolbarToday;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.tv_today_content)
    TextView tvTodayContent;
    @BindView(R.id.vp_today)
    ViewPager vpToday;
    @BindView(R.id.pt_below)
    PagerTitleStrip ptBelow;
    private List<TodayOfHistoryDetailBean.ResultBean.PicUrlBean> picUrl;

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

        /*************************** Activity 转场动画 ***************************/
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            switch ((int)(Math.random()*3)){
                case 0:
                    getWindow().setEnterTransition(new Explode());
                    break;
                case 1:
                    getWindow().setEnterTransition(new Slide());
                    break;
                case 2:
                    getWindow().setEnterTransition(new Fade());
                    break;
            }
        }
        setContentView(R.layout.activity_today_detail);
        ButterKnife.bind(this);

        String e_id = getIntent().getStringExtra("e_id");

        //添加左侧后退按钮
        setSupportActionBar(tbToday);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ptBelow.setGravity(Gravity.BOTTOM);

        QClitent.getInstance()
                .create(QNewsService.class, Constants.BASE_JUHE_URL)
                .getTodayOfHistoryDetailData(e_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TodayOfHistoryDetailBean>() {
                    @Override
                    public void accept(TodayOfHistoryDetailBean todayOfHistoryDetailBean) throws Exception {
                        // 成功获取到数据
                        if (todayOfHistoryDetailBean.getError_code() != 0) {
                            tbToday.setTitle("无结果");
                            tvTodayContent.setText("无结果");
                            return;
                        }

                        TodayOfHistoryDetailBean.ResultBean resultBean = todayOfHistoryDetailBean.getResult().get(0);
                        String content = resultBean.getContent();
                        String title = resultBean.getTitle();
                        picUrl = resultBean.getPicUrl();

                        collapsingToolbarToday.setTitle(title);
                        tvTodayContent.setText(content);

                        vpToday.setAdapter(new MyAdapter(picUrl));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // 获取数据失败
                        ToastUtils.showShortToast("获取数据失败");
                    }
                });

//        QNewsClient.getInstance().GetTodayOfHistoryDetailData(e_id, new QNewsCallback<TodayOfHistoryDetailBean>() {
//
//            @Override
//            public void onSuccess(TodayOfHistoryDetailBean response, int id) {
//
//                if (response.getError_code() != 0) {
//                    tbToday.setTitle("无结果");
//                    tvTodayContent.setText("无结果");
//                    return;
//                }
//                TodayOfHistoryDetailBean.ResultBean resultBean = response.getResult().get(0);
//                String content = resultBean.getContent();
//                String title = resultBean.getTitle();
//                picUrl = resultBean.getPicUrl();
//
//                collapsingToolbarToday.setTitle(title);
//                tvTodayContent.setText(content);
//
//                vpToday.setAdapter(new MyAdapter(picUrl));
//
//            }
//
//            @Override
//            public void onError(Exception e, int id) {
//
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyAdapter extends PagerAdapter {

        private List<TodayOfHistoryDetailBean.ResultBean.PicUrlBean> picUrl;

        public MyAdapter(List<TodayOfHistoryDetailBean.ResultBean.PicUrlBean> picUrl) {
            this.picUrl = picUrl;
        }

        @Override
        public int getCount() {
            return picUrl.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(TodayDetailActivity.this);
            LogUtils.i("url：" + picUrl.get(position).getUrl());
            Glide.with(TodayDetailActivity.this)
                    .load("http://images.juheapi.com/history/2021_1.jpg")
                    .crossFade()
                    .into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return picUrl.get(position).getPic_title();
        }
    }
}
