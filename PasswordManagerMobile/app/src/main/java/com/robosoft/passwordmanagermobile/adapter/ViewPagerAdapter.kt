package com.robosoft.viewpagersample.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.robosoft.passwordmanagermobile.fragments.SignIn
import com.robosoft.passwordmanagermobile.fragments.SignUp

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SignIn()
            1 -> SignUp()
            else -> throw Resources.NotFoundException("Position Not Found")
        }
    }

}