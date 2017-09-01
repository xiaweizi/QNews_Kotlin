package com.xiaweizi.qnews.viewadapter;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * <pre>
 *     author : xiaweizi
 *     class  : com.xiaweizi.qnews.viewadapter.ImageViewAdapter
 *     e-mail : 1012126908@qq.com
 *     time   : 2017/09/01
 *     desc   :
 * </pre>
 */

public class ImageViewAdapter {

    @BindingAdapter ({"img_url"})
    public static void loadImageView(ImageView iv, String url) {
        Glide.with(iv.getContext()).load(url).centerCrop().into(iv);
    }
}
