package com.example.foursquare.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foursquare.R
import com.example.foursquare.fragments.Details
import com.example.foursquare.fragments.Login
import com.example.foursquare.fragments.Search
import com.example.foursquare.interfaces.Communicator

class Container : AppCompatActivity(), Communicator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        val extras = intent.extras
        when (extras?.get("to")) {
            "Details" -> {
                val details = Details()
                val bundle = Bundle()
                bundle.putString("id", extras.get("id").toString())
                bundle.putString("distance", extras.get("distance").toString())
                details.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.container, details).commit()
            }

            "Search" -> {
                val search = Search()
                val bundle = Bundle()
                bundle.putDouble("latitude", extras.get("latitude") as Double)
                bundle.putDouble("longitude", extras.get("longitude") as Double)
                search.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.container, search).commit()
            }

            else -> supportFragmentManager.beginTransaction().replace(R.id.container, Login()).commit()
        }
    }

    override fun gotoHome() {
        onBackPressed()
    }

    override fun gotoHomeParticularTab(tab: Int) {
        val intent = Intent()
        intent.putExtra("tab", tab)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}