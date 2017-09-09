package com.xiaweizi.qnews.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.xiaweizi.qnews.R
import com.xiaweizi.qnews.adapter.MsgReceivedtemDelagate
import com.xiaweizi.qnews.adapter.MsgSendItemDelagate
import com.xiaweizi.qnews.adapter.RobotAdapter
import com.xiaweizi.qnews.bean.RobotMSGBean
import com.xiaweizi.qnews.commons.ActivityUtils
import com.xiaweizi.qnews.commons.Constants
import com.xiaweizi.qnews.commons.LogUtils
import com.xiaweizi.qnews.net.QClitent
import com.xiaweizi.qnews.net.QNewsService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_robot.view.*
import java.util.*

/**
 * class：   QNews
 * author：  xiaweizi
 * date：    2017/9/9 16:24
 * e-mail:   1012126908@qq.com
 * desc:
 */

class RobotFragment : Fragment(), TextView.OnEditorActionListener {

    private var adapter: RobotAdapter? = null

    private val datas = ArrayList<RobotMSGBean>()

    private var utils: ActivityUtils? = null

    private var mView: View? = null

    override fun onCreateView(
            inflater: LayoutInflater?, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        mView = inflater!!.inflate(R.layout.fragment_robot, null)

        utils = ActivityUtils(activity)

        // 设置软键盘的操作
        mView!!.et_input!!.imeOptions = EditorInfo.IME_ACTION_SEARCH
        // 设置软键盘的操作事件监听
        mView!!.et_input!!.setOnEditorActionListener(this)
        // 设置输入的类型： 文本类型
        mView!!.et_input!!.inputType = EditorInfo.TYPE_CLASS_TEXT

        adapter = RobotAdapter(activity, datas)
        adapter!!.addItemViewDelegate(MsgReceivedtemDelagate())
        adapter!!.addItemViewDelegate(MsgSendItemDelagate())

        mView!!.rv_robot!!.layoutManager = LinearLayoutManager(activity)
        mView!!.rv_robot!!.adapter = adapter

        val receiverBean = RobotMSGBean()
        receiverBean.msg = "您好！我是图灵机器人，有什么可以帮助您吗？"
        receiverBean.type = RobotMSGBean.MSG_RECEIVED
        adapter!!.addDataToAdapter(receiverBean)
        adapter!!.notifyDataSetChanged()
        mView!!.rv_robot!!.smoothScrollToPosition(adapter!!.itemCount - 1)
        mView!!.bt_send!!.setOnClickListener {
            sendMsg()
        }
        return mView
    }

    private fun sendMsg() {
        val msg = mView!!.et_input!!.text.toString()
        if (TextUtils.isEmpty(msg)) {
            mView!!.et_input!!.error = "内容不能为空"
            return
        }

        val sendBean = RobotMSGBean()
        sendBean.msg = msg
        sendBean.type = RobotMSGBean.MSG_SEND
        mView!!.et_input!!.setText("")
        adapter!!.addDataToAdapter(sendBean)
        adapter!!.notifyDataSetChanged()

        QClitent.getInstance()
                .create(QNewsService::class.java, Constants.BASE_Q_A_ROBOT_URL)
                .getQARobotData(msg)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ s ->
                    val text = s.result.text
                    val receiverBean = RobotMSGBean()
                    receiverBean.msg = text
                    receiverBean.type = RobotMSGBean.MSG_RECEIVED
                    adapter!!.addDataToAdapter(receiverBean)
                    adapter!!.notifyDataSetChanged()
                    mView!!.rv_robot!!.smoothScrollToPosition(adapter!!.itemCount - 1)
                }) { throwable -> LogUtils.i("error: " + throwable.message) }
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {

        Log.i("----->", "onEditorAction: " + actionId)
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            Log.i("---->", "IME_ACTION_SEND: ")
            sendMsg()
            return true
        } else {
            Log.i("------", "onEditorAction: ")
        }
        closeKeyBoard()

        return false
    }

    private fun closeKeyBoard() {
        mView!!.et_input!!.clearFocus()

        //关闭软键盘
        val inputMethodManager = context.getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(mView!!.et_input!!.windowToken, 0)
    }
}