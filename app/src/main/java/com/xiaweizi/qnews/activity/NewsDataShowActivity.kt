package com.xiaweizi.qnews.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.WindowManager
import android.webkit.*
import com.xiaweizi.qnews.R
import kotlinx.android.synthetic.main.activity_news_data_show.*
import me.imid.swipebacklayout.lib.app.SwipeBackActivity

/**
 * class：   QNews
 * author：  xiaweizi
 * date：    2017/9/9 15:16
 * e-mail:   1012126908@qq.com
 * desc:     新闻详情页
 */
class NewsDataShowActivity : SwipeBackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
        setContentView(R.layout.activity_news_data_show)

        val url = intent.getStringExtra("url")

        initAppBar()
        initWebView(url)
        initWebSettings()
        initWebViewClient()
        initWebChromeClient()
    }

    private fun initWebChromeClient() {
        wv_show!!.setWebChromeClient(object : WebChromeClient() {
            private var mDefaultVideoPoster: Bitmap? = null//默认得视频展示图

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                if (tb_show!= null) {
                    tb_show!!.post { tv_tb_title!!.text = if (TextUtils.isEmpty(title)) "加载中..." else title }
                }
            }

            override fun getDefaultVideoPoster(): Bitmap? {
                if (mDefaultVideoPoster == null) {
                    mDefaultVideoPoster = BitmapFactory
                            .decodeResource(resources, R.mipmap.ic_error)
                    return mDefaultVideoPoster
                }
                return super.getDefaultVideoPoster()
            }
        })
    }

    private fun initWebViewClient() {
        wv_show!!.setWebViewClient(object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
                //                mPbShow.setVisibility(View.VISIBLE);
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                //                mPbShow.setVisibility(View.GONE);
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                view.loadUrl(request.toString())
                return true
            }

            override fun onReceivedError(
                    view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)

            }
        })
    }

    private fun initWebSettings() {
        val settings = wv_show!!.settings
        //支持获取手势焦点
        wv_show!!.requestFocusFromTouch()
        //支持JS
        settings.javaScriptEnabled = true
        //支持插件
        settings.pluginState = WebSettings.PluginState.ON
        //设置适应屏幕
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        //支持缩放
        settings.setSupportZoom(true)
        //隐藏原生得缩放控件
        settings.displayZoomControls = false
        //支持内容重新布局
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.supportMultipleWindows()
        settings.setSupportMultipleWindows(true)
        //设置缓存模式
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCacheEnabled(true)
        settings.setAppCachePath(wv_show!!.context.cacheDir.absolutePath)

        //设置可访问文件
        settings.allowFileAccess = true
        //当webview调用requestFocus时为webview设置节点
        settings.setNeedInitialFocus(true)
        //支持自动加载图片
        if (Build.VERSION.SDK_INT >= 19) {
            settings.loadsImagesAutomatically = true
        } else {
            settings.loadsImagesAutomatically = false
        }
        settings.setNeedInitialFocus(true)
        //设置编码格式
        settings.defaultTextEncodingName = "UTF-8"
    }

    private fun initWebView(url: String) {
        wv_show!!.loadUrl(url)
    }

    private fun initAppBar() {
        tb_show!!.title = "载入中..."
        //        setSupportActionBar(mTbShow);
        //        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}
