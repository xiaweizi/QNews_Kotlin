package com.xiaweizi.qnews.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemLongClickListener
import com.xiaweizi.qnews.R
import com.xiaweizi.qnews.adapter.JokeAdapter
import com.xiaweizi.qnews.bean.JokeBean
import com.xiaweizi.qnews.commons.Constants
import com.xiaweizi.qnews.commons.ShareUtils
import com.xiaweizi.qnews.net.QClitent
import com.xiaweizi.qnews.net.QNewsService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * class：   QNews
 * author：  xiaweizi
 * date：    2017/9/9 15:30
 * e-mail:   1012126908@qq.com
 * desc:     段子界面
 */
class JokeFragment : Fragment() {

    private var mAdapter: JokeAdapter? = null

    internal var mData: List<JokeBean.ResultBean.DataBean> = ArrayList()
    private var mCurrentCounter: Int = 0
    private var mTotalCounter = 5

    // View
    private var mSrlJoke: SwipeRefreshLayout? = null
    private var mRvJoke: RecyclerView? = null
    private var mLlError: LinearLayout? = null
    private var mLlLoading: LinearLayout? = null
    private var mTvLoadAagin: TextView? = null

    override fun onCreateView(
            inflater: LayoutInflater?, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_joke, null)

        mSrlJoke = view.findViewById(R.id.srl_joke) as SwipeRefreshLayout?
        mRvJoke = view.findViewById(R.id.rv_joke) as RecyclerView?
        mLlError = view.findViewById(R.id.ll_error) as LinearLayout?
        mLlLoading = view.findViewById(R.id.ll_loading) as LinearLayout?
        mTvLoadAagin = view.findViewById(R.id.tv_joke_load_again) as TextView?

        //数据初始化
        mData = ArrayList<JokeBean.ResultBean.DataBean>()
        mAdapter = JokeAdapter()
        mAdapter!!.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT)

        //设置下拉刷新
        mSrlJoke!!.setColorSchemeColors(Color.RED, Color.GREEN, Color.YELLOW)
        mSrlJoke!!.setOnRefreshListener { updateDate() }

        mRvJoke!!.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL,
                false)
        mRvJoke!!.adapter = mAdapter
        mRvJoke!!.addOnItemTouchListener(object : OnItemLongClickListener() {
            override fun onSimpleItemLongClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val content = mAdapter!!.getItem(position).content
                ShareUtils.share(activity, content + "\n http://www.jianshu.com/u/d36586119d8c")
            }
        })

        mAdapter!!.setOnLoadMoreListener {
            mRvJoke!!.postDelayed(Runnable {
                if (mCurrentCounter >= mTotalCounter) {
                    //数据加载完成
                    mAdapter!!.loadMoreEnd()
                } else {

                    if (mAdapter!!.getItem(0) == null) {
                        return@Runnable
                    }

                    val unixtime = mAdapter!!.getItem(mAdapter!!.itemCount - 2)
                            .unixtime.toLong()

                    QClitent.getInstance()
                            .create(QNewsService::class.java, Constants.BASE_JOKE_URL) // 创建服务
                            .getAssignJokeData(unixtime, 1, 5, QNewsService.DESC)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ jokeBean ->
                                val data = jokeBean.result.data
                                mAdapter!!.addData(data)
                                mCurrentCounter = mTotalCounter
                                mTotalCounter += 5
                                mAdapter!!.loadMoreComplete()
                            }) { mAdapter!!.loadMoreFail() }


                }
            }, 1000)
        }
        mTvLoadAagin!!.setOnClickListener {
            updateDate()
        }
        updateDate()

        return view

    }

    private fun updateDate() {
        mSrlJoke!!.visibility = View.VISIBLE
        mLlLoading!!.visibility = View.VISIBLE
        mLlError!!.visibility = View.GONE
        mSrlJoke!!.isRefreshing = true    // 让SwipeRefreshLayout开启刷新
        QClitent.getInstance().create(QNewsService::class.java, Constants.BASE_JOKE_URL) // 创建服务
                .getCurrentJokeData(1, 8)   // 查询查询
                .subscribeOn(Schedulers.io())   //  指定被观察者的操作在io线程中完成
                .observeOn(AndroidSchedulers.mainThread())  // 指定观察者接收到数据，然后在Main线程中完成
                .subscribe({ jokeBean ->
                    // 成功获取数据
                    mLlLoading!!.visibility = View.GONE
                    mLlError!!.visibility = View.GONE
                    mAdapter!!.setNewData(jokeBean.result.data)    // 给适配器设置数据
                    mSrlJoke!!.isRefreshing = false       // 让SwipeRefreshLayout关闭刷新
                }) {
                    // 获取数据失败
                    Toast.makeText(activity, "获取数据失败!" + "访问次数上限", Toast.LENGTH_SHORT)
                            .show()
                    mSrlJoke!!.isRefreshing = false
                    mLlError!!.visibility = View.VISIBLE
                    mLlLoading!!.visibility = View.GONE
                    mSrlJoke!!.visibility = View.GONE
                }
    }

}
