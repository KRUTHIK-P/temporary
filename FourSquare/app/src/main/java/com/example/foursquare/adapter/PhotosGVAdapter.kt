package com.example.foursquare.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.foursquare.R
import com.example.foursquare.dataclass.ReviewImage
import com.example.foursquare.fragments.ReviewImagesDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

internal class PhotosGVAdapter(
    private val context: Context,
    private val reviewImageList1: List<ReviewImage>,
    private val reviewImageList: List<String>,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val activity: FragmentActivity?,
    private val placeName: String
) :
    BaseAdapter() {
    // in base adapter class we are creating variables
    // for layout inflater, course image view and course text view.
    private var layoutInflater: LayoutInflater? = null

    //    private lateinit var courseTV: TextView
    private lateinit var reviewImage: ImageView

    // below method is use to return the count of course list
    override fun getCount(): Int {
        return reviewImageList.size
    }

    // below function is use to return the item of grid view.
    override fun getItem(position: Int): Any? {
        return null
    }

    // below function is use to return item id of grid view.
    override fun getItemId(position: Int): Long {
        return 0
    }

    // in below function we are getting individual item of grid view.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.review_images_data_list_item, null)

            reviewImage = convertView!!.findViewById(R.id.reviewImage_iv)

            var url = reviewImageList[position]

            if (!url.contains("https"))
                url = "${url.substring(0..3)}s${url.substring(4)}"
            loadImageFromWebUrl(url, reviewImage)

            convertView.setOnClickListener {

                var count = 0
                var pos = 0
                for (i in reviewImageList1) {
                    count += i.image.size
                    if (position < count)
                        break
                    pos++
                }

                reviewImageList1[pos].apply {

                    val bundle = Bundle()
                    bundle.putString("reviewer_name", reviewBy)
                    bundle.putString("review_date", reviewDate)
                    bundle.putString("url", url)
                    bundle.putString("placeName", placeName)
                    bundle.putString("reviewerImage", reviewerImage)

                    val reviewImagesDetails = ReviewImagesDetails()
                    reviewImagesDetails.arguments = bundle

                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.container, reviewImagesDetails)?.addToBackStack(null)?.commit()
                }

            }
        }
        return convertView
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
}
