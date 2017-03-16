package com.xiaweizi.qnews.adapter;

import android.content.Context;

import com.xiaweizi.qnews.bean.RobotMSGBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.adapter
 * 类名：    RobotAdapter
 * 创建者：  夏韦子
 * 创建日期： 2017/2/11
 * 创建时间： 16:44
 */

public class RobotAdapter extends MultiItemTypeAdapter<RobotMSGBean> {
    private Context            context;
    private List<RobotMSGBean> datas;

    public RobotAdapter(Context context, List<RobotMSGBean> datas) {
        super(context, datas);
        this.context = context;
        this.datas = datas;
    }

    public void addDataToAdapter(RobotMSGBean bean) {
        if (datas != null) {
            datas.add(bean);
        }
    }
}
