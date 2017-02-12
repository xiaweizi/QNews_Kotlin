package com.xiaweizi.qnews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaweizi.qnews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.fragment
 * 类名：    AboutFragment
 * 创建者：  夏韦子
 * 创建日期： 2017/2/12
 * 创建时间： 13:10
 */

public class AboutFragment extends Fragment {

    @BindView(R.id.fruit_image_view)
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, null);

        ButterKnife.bind(this, view);
        Glide.with(this)
                .load("http://img.17gexing.com/uploadfile/2016/07/2/20160725115727230.jpg")
                .centerCrop()
                .into(imageView);


        return view;
    }
}
