package com.xiaweizi.qnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.activity.TodayDetailActivity;
import com.xiaweizi.qnews.bean.TodayOfHistoryBean;
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

public class TodayAdapter extends CommonAdapter<TodayOfHistoryBean.ResultBean> {

    private Context context;

    public TodayAdapter(Context context, List<TodayOfHistoryBean.ResultBean> datas) {
        super(context, R.layout.item_today, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, final TodayOfHistoryBean.ResultBean todayBean, int position) {

        holder.setText(R.id.tv_today_title, todayBean.getTitle());
        holder.setText(R.id.tv_today_date, todayBean.getDate());
        holder.setOnClickListener(R.id.ll_today_detail, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TodayDetailActivity.class);
                intent.putExtra("e_id", todayBean.getE_id());
                context.startActivity(intent);
            }
        });
    }
}
