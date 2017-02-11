package com.xiaweizi.qnews.bean;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.bean
 * 类名：    RobotMSGBean
 * 创建者：  夏韦子
 * 创建日期： 2017/2/11
 * 创建时间： 16:16
 */

public class RobotMSGBean {

    public static final int MSG_RECEIVED = 0;
    public static final int MSG_SEND = 1;

    private String msg;
    private int type;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RobotMSGBean{" +
                "msg='" + msg + '\'' +
                ", type=" + type +
                '}';
    }
}
