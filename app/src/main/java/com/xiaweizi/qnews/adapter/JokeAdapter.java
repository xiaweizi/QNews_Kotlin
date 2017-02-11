package com.xiaweizi.qnews.adapter;

import android.content.Context;

import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.bean.JokeBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.adapter
 * 类名：    JokeAdapter
 * 创建者：  夏韦子
 * 创建日期： 2017/2/11
 * 创建时间： 13:53
 */

public class JokeAdapter extends CommonAdapter<JokeBean.ResultBean.DataBean> {

    public JokeAdapter(Context context, List<JokeBean.ResultBean.DataBean> datas) {
        super(context, R.layout.item_joke, datas);
    }

    @Override
    protected void convert(ViewHolder holder, JokeBean.ResultBean.DataBean jokeBean, int position) {

        holder.setText(R.id.tv_joke_content, jokeBean.getContent())
                .setText(R.id.tv_joke_date, jokeBean.getUpdatetime());
    }
}
