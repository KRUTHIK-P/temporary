package com.robosoft.passwordmanagermobile.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.robosoft.passwordmanagermobile.R

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val logoImageView = findViewById<ImageView>(R.id.splashScreen_iv)

        logoImageView.alpha = 0f
        logoImageView.animate().setDuration(3000).alpha(1f).withEndAction {
            startActivity(Intent(this, SignInSignUp::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

}