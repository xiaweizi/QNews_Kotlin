package com.xiaweizi.qnews.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.adapter.JokeAdapter;
import com.xiaweizi.qnews.bean.JokeBean;
import com.xiaweizi.qnews.net.QClitent;
import com.xiaweizi.qnews.net.QNewsCallback;
import com.xiaweizi.qnews.net.QNewsClient;
import com.xiaweizi.qnews.net.QNewsService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.fragment
 * 类名：    JokeFragment
 * 创建者：  夏韦子
 * 创建日期： 2017/2/11
 * 创建时间： 13:44
 */

public class JokeFragment extends Fragment {

    @BindView(R.id.tb_joke)
    Toolbar tbJoke;
    @BindView(R.id.rv_joke)
    RecyclerView rvJoke;
    @BindView(R.id.srl_joke)
    SwipeRefreshLayout srlJoke;

    private JokeAdapter mAdapter;

    List<JokeBean.ResultBean.DataBean> mData;

    private int mCurrentCounter;
    private int mTotalCounter = 5;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joke, null);
        ButterKnife.bind(this, view);

        //数据初始化
        mData = new ArrayList<>();
        mAdapter = new JokeAdapter();
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);

        //设置下拉刷新
        srlJoke.setColorSchemeColors(Color.RED, Color.GREEN, Color.YELLOW);
        srlJoke.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateDate();
            }
        });

        rvJoke.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvJoke.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {

            @Override
            public void onLoadMoreRequested() {
                rvJoke.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrentCounter >= mTotalCounter) {
                            //数据加载完成
                            mAdapter.loadMoreEnd();
                        } else {

                            if (mAdapter.getItem(0) == null) {
                                return;
                            }

                            int unixtime = mAdapter.getItem(mAdapter.getItemCount()-2).getUnixtime();
                            QNewsClient.getInstance().GetNowJokeData(unixtime + "", new QNewsCallback<JokeBean>() {
                                @Override
                                public void onSuccess(JokeBean response, int id) {
                                    List<JokeBean.ResultBean.DataBean> data = response.getResult().getData();
                                    mAdapter.addData(data);
                                    mCurrentCounter = mTotalCounter;
                                    mTotalCounter += 5;
                                    mAdapter.loadMoreComplete();

                                }

                                @Override
                                public void onError(Exception e, int id) {
                                    mAdapter.loadMoreFail();
                                }
                            });

                        }
                    }
                }, 1000);
            }
        });

        updateDate();

        return view;

    }

    private void updateDate() {
        srlJoke.setRefreshing(true);

        QClitent.getInstance()
                .create(QNewsService.class)
                .getCurrentJokeData(1, 8)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JokeBean>() {
                    @Override
                    public void accept(JokeBean jokeBean) throws Exception {
                        mAdapter.setNewData(jokeBean.getResult().getData());
                        srlJoke.setRefreshing(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        srlJoke.setRefreshing(false);
                    }
                });

//        QNewsClient.getInstance().GetNowJokeData(1, 8, new QNewsCallback<JokeBean>() {
//            @Override
//            public void onSuccess(JokeBean response, int id) {
//                mAdapter.setNewData(response.getResult().getData());
//                srlJoke.setRefreshing(false);
//            }
//
//            @Override
//            public void onError(Exception e, int id) {
//                srlJoke.setRefreshing(false);
//            }
//        });
    }


}
