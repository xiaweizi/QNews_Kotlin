package com.xiaweizi.qnews.adapter;

import com.bumptech.glide.Glide;
import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.bean.RobotMSGBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.fragment
 * 类名：    MsgSendItemDelagate
 * 创建者：  夏韦子
 * 创建日期： 2017/2/11
 * 创建时间： 16:21
 */

public class MsgSendItemDelagate implements ItemViewDelegate<RobotMSGBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_robot_send;
    }

    @Override
    public boolean isForViewType(RobotMSGBean item, int position) {
        return item.getType() == RobotMSGBean.MSG_SEND;
    }

    @Override
    public void convert(ViewHolder holder, RobotMSGBean robotMSGBean, int position) {
        holder.setText(R.id.tv_msg_right, robotMSGBean.getMsg());

    }
}
