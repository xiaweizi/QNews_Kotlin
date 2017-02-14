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
import com.xiaweizi.qnews.adapter.GifAdapter;
import com.xiaweizi.qnews.bean.GIFBean;
import com.xiaweizi.qnews.net.QNewsCallback;
import com.xiaweizi.qnews.net.QNewsClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.fragment
 * 类名：    GIFFragment
 * 创建者：  夏韦子
 * 创建日期： 2017/2/13
 * 创建时间： 17:31
 */

public class GIFFragment extends Fragment {

    @BindView(R.id.rv_gif)
    RecyclerView rvGif;

    private GifAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gif, null);
        ButterKnife.bind(this, view);

        rvGif.setLayoutManager(new LinearLayoutManager(getActivity()));
        QNewsClient.getInstance().GetGIFRandomData(new QNewsCallback<GIFBean>() {
            @Override
            public void onSuccess(GIFBean response, int id) {
                List<GIFBean.ResultBean> result = response.getResult();
                adapter = new GifAdapter(getActivity(), result);
                rvGif.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e, int id) {

            }
        });

        return view;
    }
}
