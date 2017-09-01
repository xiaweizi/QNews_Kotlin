package com.xiaweizi.qnews.viewmodule;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.xiaweizi.qnews.BR;

/**
 * <pre>
 *     author : xiaweizi
 *     class  : com.xiaweizi.qnews.viewmodule.AboutModule
 *     e-mail : 1012126908@qq.com
 *     time   : 2017/09/01
 *     desc   :
 * </pre>
 */

public class AboutModule extends BaseObservable {
    private String imgUrl;

    @Bindable
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        notifyPropertyChanged(BR.imgUrl);
    }

    @NonNull
    public String getString(String text) {
        String[] split = text.split(":");
        return split[1].trim() + ":" + split[2].trim();
    }
}
