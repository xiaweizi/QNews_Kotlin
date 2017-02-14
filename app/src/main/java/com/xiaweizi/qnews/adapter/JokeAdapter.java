package com.xiaweizi.qnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.bean.JokeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.adapter
 * 类名：    JokeAdapter
 * 创建者：  夏韦子
 * 创建日期： 2017/2/11
 * 创建时间： 13:53
 */

//JokeBean.ResultBean.DataBean
//holder.setText(R.id.tv_joke_content, jokeBean.getContent())
//        .setText(R.id.tv_joke_date, jokeBean.getUpdatetime());

public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.ViewHolder> {

    private Context context;

    private List<JokeBean.ResultBean.DataBean> mData;

    public JokeAdapter(Context context) {
        this.context = context;
        mData = new ArrayList<>();
    }

    public void addDataToAdapter(JokeBean.ResultBean.DataBean bean) {
        addDataToAdapter(bean, true);
    }

    public JokeBean.ResultBean.DataBean getFirstData(){



        return mData == null ? null : mData.get(0);

    }

    public void addAllDataToAdapter(List<JokeBean.ResultBean.DataBean> data, boolean isClear, boolean isLast) {

        if (data != null) {
            if (isClear) {
                mData.clear();
                if (isLast) {
                    mData.addAll(data);
                } else {
                    mData.addAll(0, data);
                }
            } else {
                if (isLast) {
                    mData.addAll(data);
                } else {
                    mData.addAll(0, data);
                }
            }
        }

    }

    public void addAllDataToAdapter(List<JokeBean.ResultBean.DataBean> data) {
        addAllDataToAdapter(data, false, true);
    }

    public void addDataToAdapter(JokeBean.ResultBean.DataBean bean, boolean isLast) {
        if (isLast) {
            //放在最后
            mData.add(bean);
        } else {
            mData.add(1, bean);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_joke, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvDate.setText(mData.get(position).getUpdatetime());
        holder.tvContent.setText(mData.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.tv_joke_content);
            tvDate = (TextView) itemView.findViewById(R.id.tv_joke_date);
        }
    }
}
