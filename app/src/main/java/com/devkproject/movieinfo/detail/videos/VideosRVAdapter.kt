package com.devkproject.movieinfo.detail.videos

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.YOUTUBE_BASE_URL
import com.devkproject.movieinfo.api.YOUTUBE_THUMBNAIL_BASE_URL
import com.devkproject.movieinfo.api.YOUTUBE_THUMBNAIL_URL_JPG
import com.devkproject.movieinfo.model.TMDBTrailers
import kotlinx.android.synthetic.main.videos_item.view.*

class VideosRVAdapter (private val item: ArrayList<TMDBTrailers>, val context: Context): RecyclerView.Adapter<VideosRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.videos_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position], context)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(it: TMDBTrailers, context: Context) {
            val videoKey = it.key
            val videoUrl = YOUTUBE_BASE_URL + videoKey
            itemView.videos_name.text = it.name
            val videoThumbnail: String = YOUTUBE_THUMBNAIL_BASE_URL + videoKey + YOUTUBE_THUMBNAIL_URL_JPG
            Glide.with(itemView.context)
                .load(videoThumbnail)
                .into(itemView.videos_imageView)

            itemView.videos_playImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                context.startActivity(intent)
            }
        }
    }
}