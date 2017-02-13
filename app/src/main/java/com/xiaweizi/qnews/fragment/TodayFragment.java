package com.xiaweizi.qnews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.adapter.TodayAdapter;
import com.xiaweizi.qnews.bean.TodayOfHistoryBean;
import com.xiaweizi.qnews.net.QNewsCallback;
import com.xiaweizi.qnews.net.QNewsClient;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.fragment
 * 类名：    TodayFragment
 * 创建者：  夏韦子
 * 创建日期： 2017/2/12
 * 创建时间： 15:13
 */

public class TodayFragment extends Fragment {


    @BindView(R.id.tb_today)
    Toolbar tbToday;
    @BindView(R.id.rv_today)
    RecyclerView rvToday;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private TodayAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, null);
        ButterKnife.bind(this, view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setImageResource(R.drawable.ic_joke);
            }
        });

        rvToday.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        Calendar calendar = Calendar.getInstance();
        final int month = calendar.get(Calendar.MONTH) + 1;
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        QNewsClient.getInstance().GetTodayOfHistoryData(month, day,
                new QNewsCallback<TodayOfHistoryBean>() {
                    @Override
                    public void onSuccess(TodayOfHistoryBean response, int id) {
                        List<TodayOfHistoryBean.ResultBean> result = response.getResult();
                        adapter = new TodayAdapter(getActivity(), result);
                        rvToday.setAdapter(adapter);
                        tbToday.setTitle("历史上的今天 (" + month + "月" + day + "日)");
                    }

                    @Override
                    public void onError(Exception e, int id) {

                    }
                });

        return view;
    }
}
