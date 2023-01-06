package com.example.foursquare.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.foursquare.R

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    //private lateinit var splashScreenBinding: SplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.splash_screen)

        findViewById<ImageView>(R.id.splash_iv).apply {
            alpha = 0f
            animate().setDuration(3000).alpha(1f).withEndAction {
                startActivity(Intent(this@SplashScreen, HomeScreen::class.java))
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
    }
}