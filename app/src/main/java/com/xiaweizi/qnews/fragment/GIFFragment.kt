package com.xiaweizi.qnews.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.xiaweizi.qnews.R
import com.xiaweizi.qnews.adapter.GifAdapter
import com.xiaweizi.qnews.commons.Constants
import com.xiaweizi.qnews.net.QClitent
import com.xiaweizi.qnews.net.QNewsService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_gif.view.*

/**
 * class：   QNews
 * author：  xiaweizi
 * date：    2017/9/9 15:28
 * e-mail:   1012126908@qq.com
 * desc:     GIF 界面
 */
class GIFFragment : Fragment() {

    private var adapter: GifAdapter? = null
//    private var mRvGif: RecyclerView? = null

    override fun onCreateView(
            inflater: LayoutInflater?, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_gif, null)
//        mRvGif = view.findViewById(R.id.rv_gif) as RecyclerView?
        view.rv_gif!!.layoutManager = LinearLayoutManager(activity)
        QClitent.getInstance()
                .create(QNewsService::class.java, Constants.BASE_JUHE_URL)
                .gifRandomData
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ gifBean ->
                    val result = gifBean.result
                    adapter = GifAdapter(activity, result)
                    view.rv_gif!!.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                }) { throwable ->
                    Toast.makeText(activity, "获取数据失败", Toast.LENGTH_SHORT).show()
                    Log.i("----->", "error accept:${throwable.message}")
                }
        return view
    }


}
