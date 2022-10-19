package com.robosoft.passwordmanagermobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robosoft.passwordmanagermobile.R
import com.robosoft.passwordmanagermobile.dataclass.Sites

class HomeScreenAdapter(private val siteArrayList: ArrayList<Sites>) : RecyclerView.Adapter<HomeScreenAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeScreenAdapter.ViewHolder, position: Int) {
        val itemsViewModel = siteArrayList[position]

        holder.siteImage.setImageResource(itemsViewModel.siteImage)
        holder.siteName.text = itemsViewModel.siteName
        holder.copyImg.setImageResource(itemsViewModel.copyImg)
        holder.copyPassword.text = itemsViewModel.copyPassword
        holder.txtLink.text = itemsViewModel.txtLink
    }

    override fun getItemCount(): Int {
        return siteArrayList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val siteImage: ImageView = itemView.findViewById(R.id.websiteLogo_iv)
        val siteName: TextView = itemView.findViewById(R.id.websiteName_tv)
        val copyImg: ImageView = itemView.findViewById(R.id.copy_iv)
        val copyPassword: TextView = itemView.findViewById(R.id.copy_tv)
        val txtLink: TextView = itemView.findViewById(R.id.website_tv)
    }
}