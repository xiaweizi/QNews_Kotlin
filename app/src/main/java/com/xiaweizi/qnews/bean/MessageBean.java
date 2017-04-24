package com.xiaweizi.qnews.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * 功能描述：    自定义消息类型
 * 类名：       com.xiaweizi.qnews.bean.MessageBean
 * 创建者：      夏韦子
 * 创建时间：    2017/4/24 13:27
 * 项目名称：    QNews
 */
public class MessageBean implements Parcelable{

    public int type;// 消息类型
    public String content;// 消息内容

    protected MessageBean(Parcel in) {
        this.type = in.readInt();
        this.content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(content);
    }

    public static final Creator<MessageBean> CREATOR = new Creator<MessageBean>() {
        @Override
        public MessageBean createFromParcel(Parcel in) {
            return new MessageBean(in);
        }

        @Override
        public MessageBean[] newArray(int size) {
            return new MessageBean[size];
        }
    };
}