package com.example.foursquare.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.foursquare.R
import com.example.foursquare.activities.Container
import com.example.foursquare.dataclass.AddFavouriteResponse
import com.example.foursquare.dataclass.GetFavouritesResponse
import com.example.foursquare.dataclass.Id
import com.example.foursquare.dataclass.NearYouResponse
import com.example.foursquare.storage.SharedPreferencesManager
import com.example.foursquare.viewmodels.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import kotlin.math.roundToInt

class NearYouRecyclerViewAdapter(
    private val activity: FragmentActivity?,
    private val nearYouResponse: NearYouResponse,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val viewLifecycleOwner: LifecycleOwner,
    private val fragment: Fragment,
    private val getFavouritesResponse: GetFavouritesResponse,
) :
    RecyclerView.Adapter<NearYouRecyclerViewAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var viewModel: LoginViewModel

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val placeImage: ImageView = itemView.findViewById(R.id.placeImage_iv)
        val placeName: TextView = itemView.findViewById(R.id.placeName_tv)
        val rating: TextView = itemView.findViewById(R.id.rating_tv)
        val description: TextView = itemView.findViewById(R.id.description_tv)
        val description1: TextView = itemView.findViewById(R.id.description1_tv)
        val favourite: ConstraintLayout = itemView.findViewById(R.id.favourite)
        val favouriteNotSelected: ImageView = itemView.findViewById(R.id.favourite_iv)
        val favouriteSelected: ImageView = itemView.findViewById(R.id.favouriteSelected_iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.near_you_datalist_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = nearYouResponse[position]
        var url = data.placeImage

        if (!url.contains("https"))
            url = "${url.substring(0..3)}s${url.substring(4)}"

        loadImageFromWebUrl(url, holder.placeImage)
        val distance = ((data.distance.calculated / 1609) * 100).roundToInt() / 100.0
        holder.apply {
            placeName.text = data.placeName
            rating.text = ((data.rating * 10).roundToInt() / 10.0).toString()
            val s = when (data.priceRange) {
                1 -> "₹"
                2 -> "₹₹"
                3 -> "₹₹₹"
                else -> "₹₹₹₹"
            }

            description.text = "${data.category} $s \t${distance}km"
            description1.text = data.address
        }

        holder.favouriteNotSelected.visibility = View.VISIBLE
        holder.favouriteSelected.visibility = View.GONE

        var favourite = false
        for (i in getFavouritesResponse){
            if (data._id == i.placeId){
                holder.favouriteNotSelected.visibility = View.GONE
                holder.favouriteSelected.visibility = View.VISIBLE
                favourite = true
            }

        }

        holder.favourite.setOnClickListener {

            val sharedPreferencesManager = SharedPreferencesManager.getInstance(context)
            val isLoggedIn = sharedPreferencesManager.isLoggedIn
            if (isLoggedIn) {
                val id = Id(data._id)
                initViewModel()
                sharedPreferencesManager.tokenManager.token?.let { it1 -> addFavourite(it1, id) }
//                if (msg.contains("added")){
//                    holder.favouriteNotSelected.visibility = View.GONE
//                    holder.favouriteSelected.visibility = View.VISIBLE
//                }
//                else{
//                    holder.favouriteNotSelected.visibility = View.VISIBLE
//                    holder.favouriteSelected.visibility = View.GONE
//                }
                if (!favourite) {
                    holder.favouriteNotSelected.visibility = View.GONE
                    holder.favouriteSelected.visibility = View.VISIBLE
                    favourite = true
                    Toast.makeText(context, "favourite added", Toast.LENGTH_SHORT).show()

                } else {
                    holder.favouriteNotSelected.visibility = View.VISIBLE
                    holder.favouriteSelected.visibility = View.GONE
                    favourite = false
                    Toast.makeText(context, "favourite removed", Toast.LENGTH_SHORT).show()
                }


            } else {
                Toast.makeText(context, "Please login to save favourites", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(activity, Container::class.java)
            intent.putExtra("to", "Details")
            intent.putExtra("id", data._id)
            intent.putExtra("distance", distance)
            activity?.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return nearYouResponse.size
    }

    private fun loadImageFromWebUrl(url: String, placeImage: ImageView) {
        lifecycleScope.launch(Dispatchers.IO) {
            val image = try {
                val iStream = java.net.URL(url).content as InputStream
                Drawable.createFromStream(iStream, "placeImage")
            } catch (e: Exception) {
                null
            }

            image?.let {
                withContext(Dispatchers.Main) {
                    placeImage.background = it
                }
            }
        }
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(fragment)[LoginViewModel::class.java]
        viewModel.addFavouriteObserver()
            .observe(viewLifecycleOwner, Observer<AddFavouriteResponse?> {

                if (it == null) {
                    Toast.makeText(context, "Not added to favourite", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("responseeeeeeeeee", it.toString())
                }
            })
    }

    private fun addFavourite(token: String, id: Id) {
        viewModel.addFavourite(token, id)
    }

}
