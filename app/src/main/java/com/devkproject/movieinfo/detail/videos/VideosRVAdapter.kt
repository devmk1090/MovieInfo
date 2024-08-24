package com.devkproject.movieinfo.detail.videos

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.api.YOUTUBE_BASE_URL
import com.devkproject.movieinfo.api.YOUTUBE_THUMBNAIL_BASE_URL
import com.devkproject.movieinfo.api.YOUTUBE_THUMBNAIL_URL_JPG
import com.devkproject.movieinfo.databinding.VideosItemBinding
import com.devkproject.movieinfo.model.TMDBTrailers

class VideosRVAdapter (private val item: ArrayList<TMDBTrailers>, val context: Context): RecyclerView.Adapter<VideosRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VideosItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position], context)
    }

    class ViewHolder(private val binding: VideosItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(it: TMDBTrailers, context: Context) {
            val videoKey = it.key
            val videoUrl = YOUTUBE_BASE_URL + videoKey
            binding.videosName.text = it.name
            val videoThumbnail: String = YOUTUBE_THUMBNAIL_BASE_URL + videoKey + YOUTUBE_THUMBNAIL_URL_JPG
            Glide.with(itemView.context)
                .load(videoThumbnail)
                .into(binding.videosImageView)

            binding.videosPlayImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                context.startActivity(intent)
            }
        }
    }
}