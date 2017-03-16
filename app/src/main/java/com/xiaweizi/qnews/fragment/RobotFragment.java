package com.xiaweizi.qnews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaweizi.qnews.R;
import com.xiaweizi.qnews.adapter.MsgReceivedtemDelagate;
import com.xiaweizi.qnews.adapter.MsgSendItemDelagate;
import com.xiaweizi.qnews.adapter.RobotAdapter;
import com.xiaweizi.qnews.bean.RobotBean;
import com.xiaweizi.qnews.bean.RobotMSGBean;
import com.xiaweizi.qnews.commons.ActivityUtils;
import com.xiaweizi.qnews.commons.Constants;
import com.xiaweizi.qnews.commons.LogUtils;
import com.xiaweizi.qnews.net.QClitent;
import com.xiaweizi.qnews.net.QNewsService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.fragment
 * 类名：    RobotFragment
 * 创建者：  夏韦子
 * 创建日期： 2017/2/11
 * 创建时间： 16:12
 */

public class RobotFragment extends Fragment implements TextView.OnEditorActionListener {

    @BindView (R.id.tb_robot)
    Toolbar      tbRobot;
    @BindView (R.id.rv_robot)
    RecyclerView rvRobot;
    @BindView (R.id.et_input)
    EditText     etInput;

    private RobotAdapter adapter;

    private List<RobotMSGBean> datas = new ArrayList<>();

    private ActivityUtils utils;


    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_robot, null);
        ButterKnife.bind(this, view);

        utils = new ActivityUtils(getActivity());

        // 设置软键盘的操作
        etInput.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        // 设置软键盘的操作事件监听
        etInput.setOnEditorActionListener(this);
        // 设置输入的类型： 文本类型
        etInput.setInputType(EditorInfo.TYPE_CLASS_TEXT);

        adapter = new RobotAdapter(getActivity(), datas);
        adapter.addItemViewDelegate(new MsgReceivedtemDelagate());
        adapter.addItemViewDelegate(new MsgSendItemDelagate());

        rvRobot.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRobot.setAdapter(adapter);

        RobotMSGBean receiverBean = new RobotMSGBean();
        receiverBean.setMsg("您好！我是图灵机器人，有什么可以帮助您吗？");
        receiverBean.setType(RobotMSGBean.MSG_RECEIVED);
        adapter.addDataToAdapter(receiverBean);
        adapter.notifyDataSetChanged();
        rvRobot.smoothScrollToPosition(adapter.getItemCount() - 1);

        return view;
    }


    @OnClick (R.id.bt_send)
    public void onClick(View view) {
        sendMsg();

    }

    private void sendMsg() {
        String msg = etInput.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            etInput.setError("内容不能为空");
            return;
        }

        RobotMSGBean sendBean = new RobotMSGBean();
        sendBean.setMsg(msg);
        sendBean.setType(RobotMSGBean.MSG_SEND);
        etInput.setText("");
        adapter.addDataToAdapter(sendBean);
        adapter.notifyDataSetChanged();

        QClitent.getInstance()
                .create(QNewsService.class, Constants.BASE_Q_A_ROBOT_URL)
                .getQARobotData(msg)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RobotBean>() {
                    @Override
                    public void accept(RobotBean s) throws Exception {
                        String       text         = s.getResult().getText();
                        RobotMSGBean receiverBean = new RobotMSGBean();
                        receiverBean.setMsg(text);
                        receiverBean.setType(RobotMSGBean.MSG_RECEIVED);
                        adapter.addDataToAdapter(receiverBean);
                        adapter.notifyDataSetChanged();
                        rvRobot.smoothScrollToPosition(adapter.getItemCount() - 1);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.i("error: " + throwable.getMessage());
                    }
                });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        Log.i("----->", "onEditorAction: " + actionId);
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            Log.i("---->", "IME_ACTION_SEND: ");
            sendMsg();
            return true;
        } else {
            Log.i("------", "onEditorAction: ");
        }
        closeKeyBoard();

        return false;
    }

    private void closeKeyBoard() {
        etInput.clearFocus();

        //关闭软键盘
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
    }
}
