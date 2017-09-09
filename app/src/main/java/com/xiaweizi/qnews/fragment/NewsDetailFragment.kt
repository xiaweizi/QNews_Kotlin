package com.xiaweizi.qnews.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.SimpleClickListener
import com.xiawei.webviewlib.WebViewActivity
import com.xiaweizi.qnews.R
import com.xiaweizi.qnews.adapter.NewsDataAdapter
import com.xiaweizi.qnews.bean.NewsDataBean
import com.xiaweizi.qnews.commons.Constants
import com.xiaweizi.qnews.commons.ShareUtils
import com.xiaweizi.qnews.net.QClitent
import com.xiaweizi.qnews.net.QNewsService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_new_detail.view.*

/**
 * class：   QNews
 * author：  xiaweizi
 * date：    2017/9/9 16:37
 * e-mail:   1012126908@qq.com
 * desc:
 */

class NewsDetailFragment : BaseFragment {

    private var mAdapter: NewsDataAdapter? = null

    /**
     * 新闻数据类型
     */
    private var type: String? = null

    private var mView: View? = null

    constructor()

    constructor(type: String) {
        this.type = type
    }

    override fun onCreateView(
            inflater: LayoutInflater?, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        mView = inflater!!.inflate(R.layout.fragment_new_detail, null)

        mAdapter = NewsDataAdapter()
        mAdapter!!.openLoadAnimation(BaseQuickAdapter.SCALEIN)

        /*************************** 设置下拉刷新  */
        mView!!.srl!!.setColorSchemeColors(Color.RED, Color.RED)
        mView!!.srl!!.setOnRefreshListener { updateData() }

        /*************************** recyclerView 初始化数据 */
        mView!!.rv_new_detail!!.adapter = mAdapter
        mView!!.rv_new_detail!!.layoutManager = LinearLayoutManager(activity)
        mView!!.rv_new_detail!!.addOnItemTouchListener(object : SimpleClickListener() {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {}

            override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val title = mAdapter!!.getItem(position).title
                val url = mAdapter!!.getItem(position).url
                ShareUtils.share(activity, title + "\n" + url)
            }

            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                WebViewActivity.startUrl(activity,
                        (adapter.getItem(
                                position) as NewsDataBean.ResultBean.DataBean).url)
            }

            override fun onItemChildLongClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {}
        })


        return mView
    }

    override fun fetchData() {
        updateData()
    }

    fun updateData() {
        mView!!.srl!!.isRefreshing = true

        QClitent.getInstance()
                .create(QNewsService::class.java, Constants.BASE_JUHE_URL)
                .getNewsData(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ newsDataBean ->
                    mAdapter!!.setNewData(newsDataBean.result.data)
                    mView!!.srl!!.isRefreshing = false
                }) { mView!!.srl!!.isRefreshing = false }

    }
}