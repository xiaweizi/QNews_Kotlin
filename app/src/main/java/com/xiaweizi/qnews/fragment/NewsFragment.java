package com.xiaweizi.qnews.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaweizi.qnews.R;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻数据 fragment
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.fragment
 * 类名：    NewsFragment
 * 创建者：  夏韦子
 * 创建日期： 2017/2/10
 * 创建时间： 10:58
 */

public class NewsFragment extends Fragment {

    @BindView (R.id.main_viewpager)
    ViewPager mainViewpager;        //新闻数据 ViewPager

    private String[]             types;         //顶部 tab 英文内容数组
    private String[]             typesCN;       //顶部 tab 中文内容数组
    private NewsViewPagerAadpter newsViewPagerAadpter;    //ViewPager 适配器


    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsdata, null);
        ButterKnife.bind(this, view);

        types = getResources().getStringArray(R.array.news_type_en);
        typesCN = getResources().getStringArray(R.array.news_type_cn);

        //初始化ViewPager，设置适配器
        newsViewPagerAadpter = new NewsViewPagerAadpter(getActivity().getSupportFragmentManager());
        mainViewpager.setAdapter(newsViewPagerAadpter);

        /*************************** 顶部指示器数据加载 ***************************/
        MagicIndicator  magicIndicator  = (MagicIndicator) view.findViewById(R.id.magic_indicator);
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return typesCN == null ? 0 : types.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView
                        = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.BLACK);
                colorTransitionPagerTitleView.setSelectedColor(Color.RED);
                colorTransitionPagerTitleView.setText(typesCN[index]);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainViewpager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mainViewpager);
        /******************************************************/

        return view;
    }


    private class NewsViewPagerAadpter extends FragmentStatePagerAdapter {

        public NewsViewPagerAadpter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //根据位置创建相应的fragment
            return new NewsDetailFragment(types[position]);
        }

        @Override
        public int getCount() {
            return types.length;
        }

    }

}
