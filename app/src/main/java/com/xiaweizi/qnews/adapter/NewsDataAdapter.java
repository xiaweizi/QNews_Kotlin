package com.xiaweizi.qnews.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.bean.NewsDataBean;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.adapter
 * 类名：    NewsDataAdapter
 * 创建者：  夏韦子
 * 创建日期： 2017/2/10
 * 创建时间： 15:23
 */

public class NewsDataAdapter
        extends BaseQuickAdapter<NewsDataBean.ResultBean.DataBean, BaseViewHolder> {

    public NewsDataAdapter() {
        super(R.layout.item_news_detail);
    }

    @Override
    protected void convert(BaseViewHolder holder, NewsDataBean.ResultBean.DataBean dataBean) {
        holder.setText(R.id.tv_news_detail_title, dataBean.getTitle());
        holder.setText(R.id.tv_news_detail_author_name, dataBean.getAuthor_name());
        holder.setText(R.id.tv_news_detail_date, dataBean.getDate());
        holder.addOnClickListener(R.id.ll_news_detail);
        Glide.with(mContext)
             .load(dataBean.getThumbnail_pic_s())
             .placeholder(R.mipmap.ic_error)
             .error(R.mipmap.ic_error)
             .crossFade()
             .centerCrop()
             .into((ImageView) holder.getView(R.id.iv_news_detail_pic));
    }

    //    private Context context;
    //
    //    private List<NewsDataBean.ResultBean.DataBean> datas;
    //
    //    public NewsDataAdapter(Context context, List<NewsDataBean.ResultBean.DataBean> datas) {
    //        super(context, R.layout.item_news_detail, datas);
    //        this.context = context;
    //        this.datas = datas;
    //    }
    //
    //    public void addDataToAadpter(List<NewsDataBean.ResultBean.DataBean> datas){
    //        if (datas != null){
    //            datas.clear();
    //            datas.addAll(datas);
    //        }
    //    }
    //
    //    @Override
    //    protected void convert(final ViewHolder holder, final NewsDataBean.ResultBean.DataBean dataBean, int position) {
    //        holder.setText(R.id.tv_news_detail_title, dataBean.getTitle());
    //        holder.setText(R.id.tv_news_detail_author_name, dataBean.getAuthor_name());
    //        holder.setText(R.id.tv_news_detail_date, dataBean.getDate());
    //        LogUtils.i("convert");
    //        holder.setOnClickListener(R.id.ll_news_detail, new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    //                Intent intent = new Intent(context, NewsDataShowActivity.class);
    //                intent.putExtra("url", dataBean.getUrl());
    //                context.startActivity(intent);
    //            }
    //        });
    //        Glide.with(context).
    //                load(dataBean.getThumbnail_pic_s())
    //                .placeholder(R.mipmap.ic_error)
    //                .error(R.mipmap.ic_error)
    //                .crossFade()
    //                .centerCrop()
    //                .into((ImageView) holder.getView(R.id.iv_news_detail_pic));
    //    }
}
