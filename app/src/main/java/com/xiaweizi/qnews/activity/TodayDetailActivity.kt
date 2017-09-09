package com.xiaweizi.qnews.activity

import android.os.Build
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.view.*
import android.widget.ImageView
import com.blankj.utilcode.utils.SPUtils
import com.blankj.utilcode.utils.ToastUtils
import com.bumptech.glide.Glide
import com.xiaweizi.qnews.R
import com.xiaweizi.qnews.bean.TodayOfHistoryDetailBean
import com.xiaweizi.qnews.commons.Constants
import com.xiaweizi.qnews.commons.LogUtils
import com.xiaweizi.qnews.net.QClitent
import com.xiaweizi.qnews.net.QNewsService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_today_detail.*

/**
 * 历史上的今天详情页
 */

class TodayDetailActivity : AppCompatActivity() {

    private var picUrl: List<TodayOfHistoryDetailBean.ResultBean.PicUrlBean> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val util = SPUtils("theme_id")
        val theme_id = util.getInt("theme_id", R.style.AppTheme)
        setTheme(theme_id)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            // 透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        // Activity 专场动画
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = when (Math.random() * 3) {
                in 0.0..1.0 -> Explode()
                in 1.0..2.0 -> Slide()
                in 2.0..3.0 -> Fade()
                else -> Slide()
            }
        }
        setContentView(R.layout.activity_today_detail)

        val e_id: String = intent.getStringExtra("e_id")

        // 添加左侧后退按钮
        setSupportActionBar(tb_today)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        pt_below.setGravity(Gravity.BOTTOM)

        QClitent.getInstance().create(QNewsService::class.java, Constants.BASE_JUHE_URL)
                .getTodayOfHistoryDetailData(e_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // 成功获取到数据
                    if (it.error_code != 0) {
                        tb_today.title = "无结果"
                        tv_today_content.text = "无结果"
                        return@subscribe
                    }
                    val resultBean = it.result[0]
                    val content = resultBean.content
                    val title = resultBean.title
                    picUrl = resultBean.picUrl

                    collapsing_toolbar_today.title = title
                    tv_today_content.text = content
                    vp_today.adapter = MyAdapter()
                }, {
                    ToastUtils.showShortToast("获取数据失败")
                })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId === android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class MyAdapter : PagerAdapter() {
        override fun getCount(): Int =
                picUrl.size


        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            val imageView = ImageView(this@TodayDetailActivity)
            val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            imageView.layoutParams = layoutParams
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            LogUtils.i("url:${picUrl[position].url}")
            Glide.with(this@TodayDetailActivity)
                    .load(picUrl[position].url)
                    .crossFade()
                    .into(imageView);
            container?.addView(imageView)
            return imageView
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            container?.removeView(`object` as View?)
        }


        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view === `object`
        }

        override fun getPageTitle(position: Int): CharSequence {
            return picUrl[position].pic_title
        }

    }
}