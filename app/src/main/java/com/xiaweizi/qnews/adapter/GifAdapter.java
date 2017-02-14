package com.xiaweizi.qnews.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.bean.GIFBean;
import com.xiaweizi.qnews.commons.LogUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.adapter
 * 类名：    GifAdapter
 * 创建者：  夏韦子
 * 创建日期： 2017/2/13
 * 创建时间： 17:33
 */

public class GifAdapter extends CommonAdapter<GIFBean.ResultBean> {
    private Context context;

    public GifAdapter(Context context, List<GIFBean.ResultBean> datas) {
        super(context, R.layout.item_gif, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, GIFBean.ResultBean gifBean, int position) {
        holder.setText(R.id.tv_gif_title, gifBean.getContent());
        String url = gifBean.getUrl();
        LogUtils.i("url?:" + url);
        if (url.endsWith("f")){
            Glide.with(context)
                    .load(gifBean.getUrl())
                    .asGif()
                    .placeholder(R.mipmap.loading)
                    .into((ImageView) holder.getView(R.id.iv_gif));
        }else {
            Glide.with(context)
                    .load(gifBean.getUrl())
                    .placeholder(R.mipmap.ic_error)
                    .into((ImageView) holder.getView(R.id.iv_gif));
        }
    }
}
