package com.example.foursquare.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foursquare.adapter.ViewPagerAdapter
import com.example.foursquare.R
import com.example.foursquare.databinding.HomeScreenBinding
import com.example.foursquare.dataclass.GetProfileResponse
import com.example.foursquare.dataclass.LatLangg
import com.example.foursquare.storage.SharedPreferencesManager
import com.example.foursquare.viewmodels.LoginViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import de.hdodenhof.circleimageview.CircleImageView

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

    //private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private var orientationChanged: Boolean = false
    private lateinit var viewModel: LoginViewModel

    private lateinit var latLangg: LatLangg
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeScreenBinding = DataBindingUtil.setContentView(this, R.layout.home_screen)


        val intent = Intent(this, Container::class.java)
        val drawerLayout: DrawerLayout = homeScreenBinding.drawerLayout
        //navView = homeScreenBinding.navView.getHeaderView(0)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)

        val sharedPreferencesManager =
            SharedPreferencesManager.getInstance(homeScreenBinding.root.context)
        val isLoggedIn = sharedPreferencesManager.isLoggedIn
        if (!sharedPreferencesManager.isLogInShown) {
            if (!isLoggedIn) {
                gotoContainerActivity(intent)
            }
        }


        if (checkPermissions(this)) {
            setCurrentLocation(this)
        }


        homeScreenBinding.apply {
            menuIv.setOnClickListener {
                drawerLayout.open()
                if (isLoggedIn) {
                    initViewModel()
                    sharedPreferencesManager.tokenManager.token?.let { getProfile(it) }
                    navView.findViewById<TextView>(R.id.logout1_tv).visibility = View.GONE
                    navView.findViewById<TextView>(R.id.logout_tv).visibility = View.VISIBLE
                }
            }

            searchIv.setOnClickListener {
                latLangg?.let {
                    intent.putExtra("to", "Search")
                    intent.putExtra("latitude", it.latitude)
                    intent.putExtra("longitude", it.longitude)
                    startActivityForResult(intent, 1)
                }
            }

            filterIv.setOnClickListener {
                intent.putExtra("to", "Filter")
                latLangg?.let {
                    intent.putExtra("latitude", it.latitude)
                    intent.putExtra("longitude", it.longitude)
                }
                startActivity(intent)
            }

        }

        homeScreenBinding.navView.getHeaderView(0).apply {
            val profileTv = findViewById<TextView>(R.id.profileName_tv)
            profileTv.setOnClickListener {
                if (profileTv.text == "Login") {
                    gotoContainerActivity(intent)
                }
            }

            findViewById<TextView>(R.id.logout_tv).setOnClickListener {
                //val sharedPreferencesManager =  SharedPreferencesManager.getInstance(homeScreenBinding.root.context)
                sharedPreferencesManager.clear()
                findViewById<TextView>(R.id.logout_tv).visibility = View.GONE
                findViewById<TextView>(R.id.logout1_tv).visibility = View.VISIBLE
                findViewById<CircleImageView>(R.id.profile_iv).setImageDrawable(
                    resources.getDrawable(
                        R.drawable.profile_icon
                    )
                )
                profileTv.text = "Login"
            }

            findViewById<TextView>(R.id.favourites_tv).setOnClickListener {
                if (isLoggedIn) {
                    intent.putExtra("to", "Favourites")
                    latLangg?.let {
                        intent.putExtra("latitude", it.latitude)
                        intent.putExtra("longitude", it.longitude)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "login to view favourites", Toast.LENGTH_SHORT).show()
                }
            }

            findViewById<TextView>(R.id.feedback_tv).setOnClickListener {
                if (isLoggedIn) {
                    intent.putExtra("to", "Feedback")
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "login to give feedback", Toast.LENGTH_SHORT).show()
                }
            }

            findViewById<TextView>(R.id.aboutUs_tv).setOnClickListener {
                intent.putExtra("to", "AboutUs")
                startActivity(intent)
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
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val tab = data?.getIntExtra("tab", 0)
            if (tab != null) {
                homeScreenBinding.viewPager2.currentItem = tab
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getProfileObserver()
            .observe(this, Observer<GetProfileResponse?> {

                if (it == null) {
                    //Toast.makeText(this@Details.context, "No Places", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("responseeeeeeeeee", it.toString())

                    homeScreenBinding.navView.getHeaderView(0).apply {
                        var url = it.userImage

                        if (!url.contains("https"))
                            url = "${url.substring(0..3)}s${url.substring(4)}"

                        Glide.with(homeScreenBinding.root.context).load(url)
                            .into(findViewById<CircleImageView>(R.id.profile_iv))
                        findViewById<TextView>(R.id.profileName_tv).text = it.userName
                    }
                }
            })
    }

    private fun getProfile(token: String) {
        viewModel.getProfile(token)
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferencesManager =
            SharedPreferencesManager.getInstance(homeScreenBinding.root.context)
        val isLoggedIn = sharedPreferencesManager.isLoggedIn
        if (isLoggedIn) {
            initViewModel()
            sharedPreferencesManager.tokenManager.token?.let { getProfile(it) }
            homeScreenBinding.navView.getHeaderView(0)
                .findViewById<TextView>(R.id.logout1_tv).visibility = View.GONE
            homeScreenBinding.navView.getHeaderView(0)
                .findViewById<TextView>(R.id.logout_tv).visibility = View.VISIBLE
        }

    }
}