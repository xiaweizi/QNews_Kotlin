package com.xiaweizi.qnews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.commons.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.fragment
 * 类名：    NewsFragment
 * 创建者：  夏韦子
 * 创建日期： 2017/2/10
 * 创建时间： 10:58
 */

public class NewsFragment extends Fragment {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.main_viewpager)
    ViewPager mainViewpager;
    private String[] types;
    private String[] typesCN;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsdata, null);
        ButterKnife.bind(this, view);

        types = getResources().getStringArray(R.array.news_type_en);
        typesCN = getResources().getStringArray(R.array.news_type_cn);

        mainViewpager.setOffscreenPageLimit(0);
        mainViewpager.setAdapter(new MyAdapter(getActivity().getSupportFragmentManager()));
        tabLayout.setupWithViewPager(mainViewpager);

        return view;
    }

    private class MyAdapter extends FragmentStatePagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            LogUtils.i(types[position]);
            return new NewsDetailFragment(types[position]);
        }

        @Override
        public int getCount() {
            return types.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return typesCN[position];
        }
    }

}
