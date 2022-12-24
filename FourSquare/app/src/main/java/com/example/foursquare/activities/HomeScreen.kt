package com.example.foursquare.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import com.example.foursquare.adapter.ViewPagerAdapter
import com.example.foursquare.R
import com.example.foursquare.databinding.HomeScreenBinding
import com.example.foursquare.dataclass.LatLangg
import com.example.foursquare.interfaces.Communicator
import com.example.foursquare.storage.SharedPreferencesManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator

//@AndroidEntryPoint
class HomeScreen : AppCompatActivity() {

    private lateinit var homeScreenBinding: HomeScreenBinding

    // FusedLocationProviderClient - Main class for receiving location updates.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // LocationRequest - Requirements for the location updates, i.e.,
// how often you should receive updates, the priority, etc.
    private lateinit var locationRequest: LocationRequest

    // LocationCallback - Called when FusedLocationProviderClient
// has a new Location
    private lateinit var locationCallback: LocationCallback

    // This will store current location info
    private var currentLocation: Location? = null

    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private var orientationChanged: Boolean = false

    private lateinit var latLangg: LatLangg
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeScreenBinding = DataBindingUtil.setContentView(this, R.layout.home_screen)


        val intent = Intent(this, Container::class.java)

        val sharedPreferencesManager =  SharedPreferencesManager.getInstance(homeScreenBinding.root.context)
        if (!sharedPreferencesManager.isLogInShown) {
            val isLoggedIn = sharedPreferencesManager.isLoggedIn
            if (!isLoggedIn) {
                gotoContainerActivity(intent)
            }
        }


        if (checkPermissions(this)) {
            setCurrentLocation(this)
        }

        val drawerLayout: DrawerLayout = homeScreenBinding.drawerLayout
        navView = homeScreenBinding.navView
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)

        homeScreenBinding.apply {
            menuIv.setOnClickListener {
                drawerLayout.open()
            }

            searchIv.setOnClickListener {
                latLangg?.let {
                    intent.putExtra("to", "Search")
                    intent.putExtra("latitude", it.latitude)
                    intent.putExtra("longitude", it.longitude)
                    startActivityForResult(intent, 1)
                }
            }
        }


    }

    private fun gotoContainerActivity(intent: Intent) {
        startActivity(intent)
    }

    private fun checkPermissions(fragmentActivity: FragmentActivity): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                fragmentActivity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PackageManager.PERMISSION_GRANTED
            )
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PackageManager.PERMISSION_GRANTED) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this,
                    "Permission Granted",
                    Toast.LENGTH_SHORT
                ).show()

                setCurrentLocation(this)
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setCurrentLocation(activity: FragmentActivity) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

        locationRequest = LocationRequest().apply {
            // Sets the desired interval for
            // active location updates.
            // This interval is inexact.
            /*interval = TimeUnit.SECONDS.toMillis(60)

            // Sets the fastest rate for active location updates.
            // This interval is exact, and your application will never
            // receive updates more frequently than this value
            fastestInterval = TimeUnit.SECONDS.toMillis(30)

            // Sets the maximum time when batched location
            // updates are delivered. Updates may be
            // delivered sooner than this interval
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)*/

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                locationResult?.lastLocation?.let {
                    currentLocation = it

                    val locationLatLng =
                        currentLocation?.let { it1 -> LatLng(it1.latitude, it1.longitude) }

                    currentLocation?.let { it2 ->
                        latLangg = LatLangg(it2.latitude, it2.longitude)

                        val tabLayout = homeScreenBinding.tabLayout
                        val viewPager2 = homeScreenBinding.viewPager2

                        viewPager2.adapter = ViewPagerAdapter(activity, latLangg)

                        TabLayoutMediator(tabLayout, viewPager2) { tab, index ->
                            tab.text = when (index) {
                                0 -> "Near you"
                                1 -> "Toppick"
                                2 -> "Popular"
                                3 -> "Lunch"
                                4 -> "Coffee"
                                else -> throw Resources.NotFoundException("Position Not Found")
                            }
                        }.attach()
                    }

                } ?: run {
                    Log.d("TAG", "Location information isn't available.")
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )

    }

    override fun onDestroy() {
        super.onDestroy()

        val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        removeTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "Location Callback removed.")
            } else {
                Log.d("TAG", "Failed to remove Location Callback.")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
         if (requestCode == 1 && resultCode == RESULT_OK){
             val tab = data?.getIntExtra("tab", 0)
             if (tab != null) {
                 homeScreenBinding.viewPager2.currentItem = tab
             }
         }
    }
}