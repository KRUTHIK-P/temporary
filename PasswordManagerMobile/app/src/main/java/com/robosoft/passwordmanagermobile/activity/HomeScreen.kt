package com.robosoft.passwordmanagermobile.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.robosoft.passwordmanagermobile.R
import com.robosoft.passwordmanagermobile.adapter.HomeScreenAdapter
import com.robosoft.passwordmanagermobile.dataclass.Sites

class HomeScreen : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var siteArrayList: ArrayList<Sites>
    lateinit var siteImage : Array<Int>
    lateinit var siteName : Array<String>
    lateinit var copyImg : Array<Int>
    lateinit var copyPassword : String
    lateinit var txtLink : Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        displaySites()

        val search: ImageView = findViewById(R.id.search_iv)
        search.setOnClickListener {

        }

        val addFloatBtn = findViewById<ImageView>(R.id.addFloatingBtn)
        addFloatBtn.setOnClickListener {
            val intent = Intent(this, AddSite::class.java)
            startActivity(intent)
        }
    }

    private fun displaySites() {
        siteImage = arrayOf(
            R.drawable.facebook,
            R.drawable.youtube,
            R.drawable.twitter,
            R.drawable.instagram,
            R.drawable.pinterest,
            R.drawable.facebook,
            R.drawable.youtube,
            R.drawable.twitter,
            R.drawable.instagram,
            R.drawable.pinterest
        )

        siteName = arrayOf(
            getString(R.string.facebook),
            getString(R.string.youtube),
            getString(R.string.twitter),
            getString(R.string.instagram),
            getString(R.string.pinterest),
            getString(R.string.facebook),
            getString(R.string.youtube),
            getString(R.string.twitter),
            getString(R.string.instagram),
            getString(R.string.pinterest)
        )

        copyImg = arrayOf(R.drawable.ic_baseline_content_copy_24)

        copyPassword = getString(R.string.copy_password)

        txtLink = arrayOf(
            getString(R.string.www_facebook_com),
            getString(R.string.www_youtube_com),
            getString(R.string.www_twitter_com),
            getString(R.string.www_instagram_com),
            getString(R.string.www_pinterest_com),
            getString(R.string.www_facebook_com),
            getString(R.string.www_youtube_com),
            getString(R.string.www_twitter_com),
            getString(R.string.www_instagram_com),
            getString(R.string.www_pinterest_com)
        )

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        siteArrayList = arrayListOf()
        getUserdata()
    }

    private fun getUserdata() {

        for (i in siteImage.indices) {
            val sites = Sites(siteImage[i], siteName[i], copyImg[0], copyPassword, txtLink[i])
            siteArrayList.add(sites)
        }

        recyclerView.adapter = HomeScreenAdapter(siteArrayList)

        val countTv = findViewById<TextView>(R.id.countTv)
        countTv.text = siteArrayList.size.toString()
    }
}