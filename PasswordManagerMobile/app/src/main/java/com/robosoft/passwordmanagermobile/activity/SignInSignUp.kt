package com.robosoft.passwordmanagermobile.activity

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.robosoft.fragmentlifecycle.Communicator
import com.robosoft.passwordmanagermobile.R
import com.robosoft.passwordmanagermobile.fragments.SignIn
import com.robosoft.viewpagersample.adapter.ViewPagerAdapter

class SignInSignUp : AppCompatActivity(), Communicator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_sign_up)

        val fragmentActivity = this

        supportActionBar?.hide()

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val viewPager2: ViewPager2 = findViewById(R.id.viewPager2)


        viewPager2.adapter = ViewPagerAdapter(fragmentActivity)

        TabLayoutMediator(tabLayout, viewPager2) { tab, index ->
            tab.text = when (index) {
                0 -> "SIGN IN"
                1 -> "SIGN UP"
                else -> throw Resources.NotFoundException("Position Not Found")
            }
        }.attach()
    }

    override fun passControl() {
        findViewById<ViewPager2>(R.id.viewPager2).setCurrentItem(0,true)
    }

    override fun passSignInControl() {
        startActivity(Intent(this,HomeScreen::class.java))
    }
}