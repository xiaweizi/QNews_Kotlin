package com.xiaweizi.qnews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.adapter.NewsDataAdapter;
import com.xiaweizi.qnews.bean.NewsDataBean;
import com.xiaweizi.qnews.commons.LogUtils;
import com.xiaweizi.qnews.net.QNewsCallback;
import com.xiaweizi.qnews.net.QNewsClient;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.fragment
 * 类名：    NewsDetailFragment
 * 创建者：  夏韦子
 * 创建日期： 2017/2/10
 * 创建时间： 15:36
 */

public class NewsDetailFragment extends Fragment {

    @BindView(R.id.rv_new_detail)
    RecyclerView rvNewDetail;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    private NewsDataAdapter mAdapter;

    private String type;
    private List<NewsDataBean.ResultBean.DataBean> data = new ArrayList<>();

    public NewsDetailFragment(String type) {
        this.type = type;
        LogUtils.i("NewsDetailFragment:--->" + type);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_detail, null);
        ButterKnife.bind(this, view);
        LogUtils.i("onCreateView");

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                QNewsClient.getInstance().GetNewsData(type, new QNewsCallback<NewsDataBean>() {
                    @Override
                    public void onSuccess(NewsDataBean response, int id) {
                        data = response.getResult().getData();
                        mAdapter.addDataToAadpter(data);
                        mAdapter.notifyDataSetChanged();
                        srl.setRefreshing(false);
                    }

                    @Override
                    public void onError(Exception e, int id) {
                    }
                });
                srl.setRefreshing(false);
            }
        });


        rvNewDetail.setLayoutManager(new LinearLayoutManager(getActivity()));

        QNewsClient.getInstance().GetNewsData(type, new QNewsCallback<NewsDataBean>() {
            @Override
            public void onSuccess(NewsDataBean response, int id) {
                data = response.getResult().getData();
                mAdapter = new NewsDataAdapter(getActivity(), data);
                rvNewDetail.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e, int id) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i("onResume");
        View view = getView();
        LogUtils.i("getview" + view.toString());
    }


}
