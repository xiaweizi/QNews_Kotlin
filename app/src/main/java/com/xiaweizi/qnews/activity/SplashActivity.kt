package com.xiaweizi.qnews.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.blankj.utilcode.utils.SPUtils
import com.xiaweizi.qnews.R
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * class：   QNews
 * author：  xiaweizi
 * date：    2017/9/9 14:58
 * e-mail:   1012126908@qq.com
 * desc:    跳转界面
 */
class SplashActivity : AppCompatActivity() {
    val TAG = "SplashActivity---->"
    val animalSet = AnimatorSet()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val util = SPUtils("theme_id")
        val theme_id: Int = util.getInt("theme_id", R.style.AppTheme)
        setTheme(theme_id)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
        setContentView(R.layout.activity_splash)

        val translationX: ObjectAnimator = ObjectAnimator.ofFloat(iv_splash, "translationX", 600f, 0f)

        val translationY = ObjectAnimator
                .ofFloat(iv_splash, "translationY", -100f, 90f, -80f, 70f, -60f, 50f)
        animalSet.playTogether(translationX, translationY)
        animalSet.duration = 2000
        addListener()
    }

    private fun addListener() {
        animalSet.start()
        animalSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                try {
                    Thread.sleep(500)
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
    }
}