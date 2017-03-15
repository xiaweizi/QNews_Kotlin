package com.xiaweizi.qnews.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.xiawei.webviewlib.WebViewActivity;
import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.adapter.NewsDataAdapter;
import com.xiaweizi.qnews.bean.NewsDataBean;
import com.xiaweizi.qnews.commons.Constants;
import com.xiaweizi.qnews.net.QClitent;
import com.xiaweizi.qnews.net.QNewsService;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 新闻详情 fragment，
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.fragment
 * 类名：    NewsDetailFragment
 * 创建者：  夏韦子
 * 创建日期： 2017/2/10
 * 创建时间： 15:36
 */

@SuppressLint("ValidFragment")
public class NewsDetailFragment extends BaseFragment {

    @BindView(R.id.rv_new_detail)
    RecyclerView rvNewDetail;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    private NewsDataAdapter mAdapter;

    /**
     * 新闻数据类型
     */
    private String type;

    public NewsDetailFragment() {
    }

    public NewsDetailFragment(String type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_detail, null);
        ButterKnife.bind(this, view);

        mAdapter = new NewsDataAdapter();
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        /*************************** 设置下拉刷新 ***************************/
        srl.setColorSchemeColors(Color.RED, Color.RED);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
            }
        });

        /*************************** recyclerView 初始化数据***************************/
        rvNewDetail.setAdapter(mAdapter);
        rvNewDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNewDetail.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                Intent intent = new Intent(getActivity(), NewsDataShowActivity.class);
//                intent.putExtra("url", ((NewsDataBean.ResultBean.DataBean)adapter.getItem(position)).getUrl());
//                getActivity().startActivity(intent);
                WebViewActivity.startUrl(getActivity(), ((NewsDataBean.ResultBean.DataBean)adapter.getItem(position)).getUrl());
            }
        });

        return view;
    }

    @Override
    public void fetchData() {
        updateData();
    }

    public void updateData() {
        srl.setRefreshing(true);

        QClitent.getInstance()
                .create(QNewsService.class, Constants.BASE_JUHE_URL)
                .getNewsData(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NewsDataBean>() {
                    @Override
                    public void accept(NewsDataBean newsDataBean) throws Exception {
                        mAdapter.setNewData(newsDataBean.getResult().getData());
                        srl.setRefreshing(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        srl.setRefreshing(false);
                    }
                });

//        QNewsClient.getInstance().GetNewsData(type, new QNewsCallback<NewsDataBean>() {
//            @Override
//            public void onSuccess(NewsDataBean response, int id) {
//                mAdapter.setNewData(response.getResult().getData());
//                srl.setRefreshing(false);
//            }
//
//            @Override
//            public void onError(Exception e, int id) {
//                srl.setRefreshing(false);
//            }
//        });
    }
}
