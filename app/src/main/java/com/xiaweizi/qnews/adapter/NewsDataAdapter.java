package com.xiaweizi.qnews.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.bean.NewsDataBean;
import com.xiaweizi.qnews.commons.LogUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.adapter
 * 类名：    NewsDataAdapter
 * 创建者：  夏韦子
 * 创建日期： 2017/2/10
 * 创建时间： 15:23
 */

public class NewsDataAdapter extends CommonAdapter<NewsDataBean.ResultBean.DataBean>{

    private Context context;

    public NewsDataAdapter(Context context, List<NewsDataBean.ResultBean.DataBean> datas) {
        super(context, R.layout.item_news_detail, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, NewsDataBean.ResultBean.DataBean dataBean, int position) {
        LogUtils.i("convert");
        holder.setText(R.id.tv_news_detail_title, dataBean.getTitle());
        holder.setText(R.id.tv_news_detail_author_name, dataBean.getAuthor_name());
        holder.setText(R.id.tv_news_detail_date, dataBean.getDate());
        Glide.with(context).
                load(dataBean.getThumbnail_pic_s())
                .crossFade()
                .centerCrop()
                .into((ImageView) holder.getView(R.id.iv_news_detail_pic));
    }
}
