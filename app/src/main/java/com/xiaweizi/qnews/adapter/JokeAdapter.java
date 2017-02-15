package com.xiaweizi.qnews.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.bean.JokeBean;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.adapter
 * 类名：    JokeAdapter
 * 创建者：  夏韦子
 * 创建日期： 2017/2/11
 * 创建时间： 13:53
 */


public class JokeAdapter extends BaseQuickAdapter<JokeBean.ResultBean.DataBean, BaseViewHolder> {

    public JokeAdapter() {
        super(R.layout.item_joke);
    }

    @Override
    protected void convert(BaseViewHolder helper, JokeBean.ResultBean.DataBean item) {
        helper.setText(R.id.tv_joke_content, item.getContent());
        helper.setText(R.id.tv_joke_date, item.getUpdatetime());
        helper.getConvertView().setOnClickListener(null);
    }

}
