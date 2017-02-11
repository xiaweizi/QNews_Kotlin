package com.xiaweizi.qnews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.adapter.JokeAdapter;
import com.xiaweizi.qnews.bean.JokeBean;
import com.xiaweizi.qnews.net.QNewsCallback;
import com.xiaweizi.qnews.net.QNewsClient;

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

    private JokeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joke, null);
        ButterKnife.bind(this, view);

        rvJoke.setLayoutManager(new LinearLayoutManager(getActivity()));

        QNewsClient.getInstance().GetNowJokeData(1, 8, new QNewsCallback<JokeBean>(){

            @Override
            public void onSuccess(JokeBean response, int id) {
                List<JokeBean.ResultBean.DataBean> data = response.getResult().getData();
                mAdapter = new JokeAdapter(getActivity(), data);
                rvJoke.setAdapter(mAdapter);
            }

            @Override
            public void onError(Exception e, int id) {

            }
        });

        return view;

    }
}
