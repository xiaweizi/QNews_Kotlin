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

import com.blankj.utilcode.utils.ToastUtils;
import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.adapter.JokeAdapter;
import com.xiaweizi.qnews.bean.JokeBean;
import com.xiaweizi.qnews.commons.LogUtils;
import com.xiaweizi.qnews.net.QNewsCallback;
import com.xiaweizi.qnews.net.QNewsClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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


    private List<JokeBean.ResultBean.DataBean> mData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joke, null);
        ButterKnife.bind(this, view);
        mData = new ArrayList<>();

        srlJoke.setColorSchemeColors(Color.RED, Color.GREEN, Color.YELLOW);
        srlJoke.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMore();
            }
        });

        rvJoke.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new JokeAdapter(getActivity());
        rvJoke.setAdapter(mAdapter);

        QNewsClient.getInstance().GetNowJokeData(1, 8, new QNewsCallback<JokeBean>() {

            @Override
            public void onSuccess(JokeBean response, int id) {
                List<JokeBean.ResultBean.DataBean> data = response.getResult().getData();
                mAdapter.addAllDataToAdapter(data);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e, int id) {

            }
        });

        return view;

    }

    private void loadMore() {
        if (mAdapter.getFirstData() == null){
            return;
        }
        int unixtime = mAdapter.getFirstData().getUnixtime();
        LogUtils.i("unixtime" + unixtime);
        QNewsClient.getInstance().GetNowJokeData(unixtime + "", new QNewsCallback<JokeBean>() {
            @Override
            public void onSuccess(JokeBean response, int id) {
                final List<JokeBean.ResultBean.DataBean> data = response.getResult().getData();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Thread.sleep(1000);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.addAllDataToAdapter(data, false, false);
                                        mAdapter.notifyDataSetChanged();
                                        srlJoke.setRefreshing(false);
                                        ToastUtils.showShortToast("刷新成功");
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


            }

            @Override
            public void onError(Exception e, int id) {

            }
        });
    }


}
