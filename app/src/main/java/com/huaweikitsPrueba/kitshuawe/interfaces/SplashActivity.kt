package com.huaweikitsPrueba.kitshuawe.interfaces

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewPropertyAnimator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.scaleMatrix
import com.huaweikitsPrueba.kitshuawe.MainActivity
import com.huaweikitsPrueba.kitshuawe.R
import com.huaweikitsPrueba.kitshuawe.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay


class SplashActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()



        starAnimation()

    }

    private fun starAnimation() {
/*        val width:Int = resources.displayMetrics.widthPixels
        val height:Int = resources.displayMetrics.heightPixels

        Log.d("SPLASHACTIVITY","widht= ${width} heigt = ${height}")*/

        binding.imgSplash.animate().apply {

                scaleX(.6f)
                scaleY(.6f)
                startDelay=250
                duration=500
                withEndAction {
                    scaleX(50f)
                    scaleY(50f)
                    duration=500
                    withEndAction {
                        binding.txtAppName.alpha=0f
                        startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                        finish()
                    }

                }
            }







    }
}