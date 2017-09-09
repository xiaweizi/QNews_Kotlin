package com.xiaweizi.qnews.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.utils.FileUtils
import com.blankj.utilcode.utils.LogUtils
import com.blankj.utilcode.utils.SPUtils
import com.bumptech.glide.Glide
import com.roughike.bottombar.BottomBar
import com.xiaweizi.qnews.R
import com.xiaweizi.qnews.commons.ActivityUtils
import com.xiaweizi.qnews.commons.VersionUtils
import com.xiaweizi.qnews.fragment.*
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import java.io.File
import java.lang.ref.WeakReference

/**
 * class：   QNews
 * author：  xiaweizi
 * date：    2017/9/9 16:49
 * e-mail:   1012126908@qq.com
 * desc:     主界面
 */
class MainActivity : AppCompatActivity() {

    private var newsFragment: NewsFragment? = null      //新闻数据
    private var jokeFragment: JokeFragment? = null      //段子
    private var robotFragment: RobotFragment? = null    //机器人
    private var aboutFragment: AboutFragment? = null    //关于
    private var todayFragment: TodayFragment? = null    //历史上的今天
    private var gifFragment: GIFFragment? = null        //动态图

    private var currentFragment: Fragment? = null

    private var utils: ActivityUtils? = null
    private var bottomBar: BottomBar? = null
    private var builder: AlertDialog.Builder? = null

    private var mSPUtils: SPUtils? = null

    private var mIconImage: ImageView? = null
    private var mDialog: BottomSheetDialog? = null
    private var mHandler: MyHandler? = null
    private var mDirSize = ""

    internal inner class MyHandler(activity: MainActivity) : Handler() {
        var mActivity: WeakReference<MainActivity>

        init {
            mActivity = WeakReference(activity)
        }

        override fun handleMessage(msg: Message) {
            val theActivity = mActivity.get()
            if (theActivity == null || theActivity.isFinishing) {
                return
            }
            // 消息处理
            when (msg.what) {
                SUCESS -> utils!!.showToast("清理成功")
                FAILED -> {
                }
                else -> {
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val util = SPUtils("theme_id")
        val theme_id = util.getInt("theme_id", R.style.AppTheme)
        setTheme(theme_id)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        //设置软键盘的模式为适应屏幕模式
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        setContentView(R.layout.activity_main)
        retrieveFragment()

        utils = ActivityUtils(this)
        mSPUtils = SPUtils("head")
        mHandler = MyHandler(this)

        Thread(Runnable { mDirSize = FileUtils.getDirSize(cacheDir) }).start()


        /*************************** 左侧 侧滑菜单 设置头像图片  */
        mIconImage = nv_left!!.getHeaderView(0).findViewById(R.id.icon_image) as ImageView
        val ivBmp = nv_left!!.getHeaderView(0).findViewById(R.id.iv_head_bg) as ImageView
        if (!mSPUtils!!.getBoolean("has_head", false)) {
            Glide.with(this)
                    .load("http://img.17gexing.com/uploadfile/2016/07/2/20160725115642623.gif")
                    .asGif()
                    .centerCrop()
                    .into(mIconImage!!)
        } else {
            mIconImage!!.setImageBitmap(BitmapFactory.decodeFile(TEMP_PATH))
        }

        OkHttpUtils.get()
                .url("http://guolin.tech/api/bing_pic")
                .build()
                .execute(object : StringCallback() {
                    override fun onError(call: Call, e: Exception, id: Int) {

                    }

                    override fun onResponse(response: String, id: Int) {
                        Glide.with(this@MainActivity)
                                .load(response)
                                .crossFade()
                                .centerCrop()
                                .into(ivBmp)
                    }
                })

        mIconImage!!.setOnClickListener {
            mDialog = BottomSheetDialog(this@MainActivity)
            val view = View.inflate(this@MainActivity,
                    R.layout.bottom_dialog_pic_selector, null)
            val xiangji = view.findViewById(R.id.tv_xiangji) as TextView
            val xiangce = view.findViewById(R.id.tv_xiangce) as TextView
            xiangce.setOnClickListener(listener)
            xiangji.setOnClickListener(listener)
            mDialog!!.setContentView(view)
            mDialog!!.setCancelable(true)
            mDialog!!.setCanceledOnTouchOutside(true)
            mDialog!!.show()
        }

        /*************************** 底部bar 设置点击事件  */
        bottomBar = findViewById(R.id.bottomBar) as BottomBar
        bottomBar!!.setOnTabSelectListener { tabId ->
            when (tabId) {
                R.id.tab_news -> {
                    LogUtils.i("setOnTabSelectListener")
                    if (newsFragment == null) newsFragment = NewsFragment()
                    switchFragment(newsFragment!!)
                    nv_left!!.setCheckedItem(R.id.nav_news)
                    closeDrawerLayout()
                }
                R.id.tab_joke -> {
                    nv_left!!.setCheckedItem(R.id.nav_duanzi)
                    if (jokeFragment == null) jokeFragment = JokeFragment()
                    switchFragment(jokeFragment!!)
                    closeDrawerLayout()
                }
                R.id.tab_today -> {
                    nv_left!!.setCheckedItem(R.id.nav_today_of_history)
                    if (todayFragment == null) todayFragment = TodayFragment()
                    switchFragment(todayFragment!!)
                    closeDrawerLayout()
                }
                R.id.tab_robot -> {
                    nv_left!!.setCheckedItem(R.id.nav_robot)
                    if (robotFragment == null) robotFragment = RobotFragment()
                    switchFragment(robotFragment!!)
                    closeDrawerLayout()
                }
                R.id.tab_about -> {
                    nv_left!!.setCheckedItem(R.id.nav_other)
                    if (aboutFragment == null) aboutFragment = AboutFragment()
                    switchFragment(aboutFragment!!)
                    closeDrawerLayout()
                }
            }
        }
        //底部bar设置再次点击事件
        bottomBar!!.setOnTabReselectListener { tabId ->
            when (tabId) {
                R.id.tab_news -> {
                }
                R.id.tab_joke -> {
                    if (gifFragment == null) gifFragment = GIFFragment()
                    switchFragment(gifFragment!!)
                }
                R.id.tab_robot -> {
                }
            }
        }


        /*************************** 左侧 侧滑菜单 设置选择事件  */
        nv_left!!.setCheckedItem(R.id.nav_news)
        nv_left!!.setNavigationItemSelectedListener { item ->
            nv_left!!.setCheckedItem(item.itemId)
            dl_activity_main!!.closeDrawers()
            when (item.itemId) {
                R.id.nav_news -> bottomBar!!.selectTabAtPosition(0, true)
                R.id.nav_duanzi -> bottomBar!!.selectTabAtPosition(1, true)
                R.id.nav_today_of_history -> bottomBar!!.selectTabAtPosition(2, true)
                R.id.nav_robot -> bottomBar!!.selectTabAtPosition(3, true)
                R.id.nav_other -> bottomBar!!.selectTabAtPosition(4, true)
                R.id.nav_clear_cache -> clearCache()
                R.id.nav_version_update -> VersionUtils.updateVersion(this@MainActivity)
                R.id.nav_change_theme -> alertChangeTheme()
                R.id.nav_day_night -> changeTheme(9)
                else -> {
                }
            }
            false
        }
    }

    /**
     * 切换Fragment的显示

     * @param target 要切换的 Fragment
     */
    private fun switchFragment(target: Fragment) {

        // 如果当前的fragment 就是要替换的fragment 就直接return
        if (currentFragment === target) return

        // 获得 Fragment 事务
        val transaction = supportFragmentManager.beginTransaction()

        // 如果当前Fragment不为空，则隐藏当前的Fragment
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }

        // 如果要显示的Fragment 已经添加了，那么直接 show
        if (target.isAdded) {
            transaction.show(target)
        } else {
            // 如果要显示的Fragment没有添加，就添加进去
            transaction.add(R.id.fl_content, target, target.javaClass.name)
        }

        // 事务进行提交
        transaction.commit()

        //并将要显示的Fragment 设为当前的 Fragment
        currentFragment = target
    }

    /**
     * 找回FragmentManager中存储的Fragment
     */
    private fun retrieveFragment() {
        val manager = supportFragmentManager
        newsFragment = manager.findFragmentByTag("NewsFragment") as NewsFragment?
        jokeFragment = manager.findFragmentByTag("JokeFragment") as JokeFragment?
        todayFragment = manager.findFragmentByTag("TodayFragment") as TodayFragment?
        robotFragment = manager.findFragmentByTag("RobotFragment") as RobotFragment?
        aboutFragment = manager.findFragmentByTag("AboutFragment") as AboutFragment?
        gifFragment = manager.findFragmentByTag("GifFragment") as GIFFragment?
    }


    /**
     * 关闭左侧 侧滑菜单
     */
    private fun closeDrawerLayout() {
        if (dl_activity_main!!.isDrawerOpen(Gravity.LEFT)) {
            dl_activity_main!!.closeDrawers()
        }
    }

    internal var lastTime: Long = 0

    /**
     * 2秒内连续点击 back 键，退出应用
     */
    override fun onBackPressed() {
        // 判断侧滑菜单是否开启，如果开启则先关闭
        if (dl_activity_main!!.isDrawerOpen(Gravity.LEFT)) {
            dl_activity_main!!.closeDrawers()
            return
        }
        // 判断当前fragment 是不是 新闻fragment，如果不是先退到新闻fragment
        if (currentFragment !== newsFragment) {
            bottomBar!!.selectTabAtPosition(0)
            return
        }

        val curTime = System.currentTimeMillis()
        if (curTime - lastTime > 2000) {
            utils!!.showToast("再按一次退出应用")
            lastTime = curTime
        } else {
            moveTaskToBack(true)
        }
    }


    private fun clearCache() {

        AlertDialog.Builder(this@MainActivity).setTitle("确定要清理缓存")
                .setMessage("缓存大小：" + mDirSize)
                .setPositiveButton("清理"
                ) { dialog, which ->
                    Thread(Runnable {
                        FileUtils.deleteDir(
                                cacheDir)
                        mHandler!!.sendEmptyMessage(
                                SUCESS)
                    }).start()
                }
                .setNegativeButton("取消", null)
                .show()
    }

    private fun changeTheme(index: Int) {
        val themes = intArrayOf(R.style.AppTheme, R.style.AppTheme_Blue, R.style.AppTheme_Green, R.style.AppTheme_Orange, R.style.AppTheme_Pink, R.style.AppTheme_Sky, R.style.AppTheme_Purple, R.style.AppTheme_PP, R.style.AppTheme_Yellow, R.style.AppTheme_Night)
        val utils = SPUtils("theme_id")
        utils.putInt("theme_id", themes[index])
        val intent = intent
        overridePendingTransition(0, 0)
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
    }


    /**
     * 动态换肤
     */
    private fun alertChangeTheme() {
        val view = View.inflate(this, R.layout.item_change_theme, null)
        builder = AlertDialog.Builder(this).setView(view)
        builder!!.show()
        view.findViewById(R.id.tv_red).setOnClickListener(listener)
        view.findViewById(R.id.tv_green).setOnClickListener(listener)
        view.findViewById(R.id.tv_blue).setOnClickListener(listener)
        view.findViewById(R.id.tv_orange).setOnClickListener(listener)
        view.findViewById(R.id.tv_pink).setOnClickListener(listener)
        view.findViewById(R.id.tv_sky).setOnClickListener(listener)
        view.findViewById(R.id.tv_purple).setOnClickListener(listener)
        view.findViewById(R.id.tv_pp).setOnClickListener(listener)
        view.findViewById(R.id.tv_yellow).setOnClickListener(listener)

    }

    private val listener = View.OnClickListener { v ->
        when (v.id) {
            R.id.tv_red -> changeTheme(0)
            R.id.tv_blue -> changeTheme(1)
            R.id.tv_green -> changeTheme(2)
            R.id.tv_orange -> changeTheme(3)
            R.id.tv_pink -> changeTheme(4)
            R.id.tv_sky -> changeTheme(5)
            R.id.tv_purple -> changeTheme(6)
            R.id.tv_pp -> changeTheme(7)
            R.id.tv_yellow -> changeTheme(8)
            R.id.tv_xiangji -> {
                //相机
                var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                // 指定调用相机拍照后照片存储路径
                val imageUri = Uri.parse(FILE_PATH)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, 1000)
            }
            R.id.tv_xiangce -> {
                //相册
                intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                startActivityForResult(intent, 1001)
            }


            else -> {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                1000 -> {
                    val temp = File(TEMP_PATH)
                    startPhotoZoom(Uri.fromFile(temp))
                }
                1001 -> {
                    val temp1 = File(TEMP_PATH)
                    startPhotoZoom(Uri.fromFile(temp1))
                }
                1002 -> if (data != null) {
                    val extras = data.extras
                    if (extras != null) {
                        val bmp = extras.getParcelable<Bitmap>("data")
                        Log.i("--->", "onActivityResult: ")
                        mIconImage!!.setImageBitmap(bmp)
                        mSPUtils!!.putBoolean("has_head", true)
                        if (mDialog != null && mDialog!!.isShowing) {
                            mDialog!!.dismiss()
                        }
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun startPhotoZoom(uri: Uri) {

        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        // crop 为true 是设置在开启的intent中设置显示的view可以裁剪
        intent.putExtra("crop", "true")

        // aspect 是宽高比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)

        // output 是裁剪图片的宽高
        intent.putExtra("outputX", 100)
        intent.putExtra("outputY", 100)
        intent.putExtra("return-data", true)
        intent.putExtra("noFaceDetection", true)
        startActivityForResult(intent, 1002)
    }

    companion object {

        private val TAG = "MainActivity------>"

        val FILE_PATH = "file://" +
                Environment.getExternalStorageDirectory().path +
                "/xiaweizi" + "/image_cache" +
                "/camera.jpg"
        val TEMP_PATH = Environment.getExternalStorageDirectory().path +
                "/xiaweizi" + "/image_cache" +
                "/camera.jpg"
        val SUCESS = 0
        val FAILED = 1
    }


}
