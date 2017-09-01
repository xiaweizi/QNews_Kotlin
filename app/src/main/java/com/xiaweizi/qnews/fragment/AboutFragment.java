package com.xiaweizi.qnews.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiawei.webviewlib.WebViewActivity;
import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.commons.LogUtils;
import com.xiaweizi.qnews.databinding.FragmentAboutBinding;
import com.xiaweizi.qnews.viewmodule.AboutModule;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.fragment
 * 类名：    AboutFragment
 * 创建者：  夏韦子
 * 创建日期： 2017/2/12
 * 创建时间： 13:10
 */

public class AboutFragment extends Fragment {

    private AboutModule          mAboutModule;
    private FragmentAboutBinding mAboutBinding;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAboutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, null, false);
        View view = mAboutBinding.getRoot();

        mAboutModule = new AboutModule();
        mAboutModule.setImgUrl(getString(R.string.about_background));
        mAboutBinding.setAbout(mAboutModule);
        mAboutBinding.setPresenter(new Presenter());

        return view;
    }

    public class Presenter{
        public void onAboutClick(View view) {
            String text = ((TextView) view).getText().toString();
            LogUtils.i("xwz-->" + text + "被点击了");
            String url  = mAboutModule.getString(text);
            WebViewActivity.startUrl(AboutFragment.this.getActivity(), url);
        }
    }
}
