package com.xiaweizi.qnews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    private NewsDataAdapter mAdapter;

    private String type;

    public NewsDetailFragment(String type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_detail, null);
        ButterKnife.bind(this, view);

        LogUtils.i("NewsDetailFragment被创建       " + type);

        rvNewDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        QNewsClient.getInstance().GetNewsData(type, new QNewsCallback() {
            @Override
            public void onSuccess(Object response, int id) {
                NewsDataBean bean = (NewsDataBean) response;
                List<NewsDataBean.ResultBean.DataBean> data = bean.getResult().getData();
                LogUtils.i("news详细结果" + data.toString());
                mAdapter = new NewsDataAdapter(getActivity(), data);
                rvNewDetail.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                LogUtils.i(Thread.currentThread().getName());
            }

            @Override
            public void onError(Exception e, int id) {

            }
        });


        return view;
    }
}
