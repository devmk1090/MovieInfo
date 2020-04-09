package com.devkproject.movieinfo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.model.MovieThumb
import kotlinx.android.synthetic.main.popular_item.view.*

class PopularRVAdapter(val thumbList: ArrayList<MovieThumb>, val context: Context)
    : RecyclerView.Adapter<PopularRVAdapter.PopularViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.popular_item, parent, false)
        return PopularViewHolder(v)
    }

    override fun getItemCount(): Int {
        return thumbList.count()
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.bind(thumbList[position], context)
    }

    class PopularViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(movie: MovieThumb, context: Context) {
            itemView.popular_thumb_title.text = movie.title
            itemView.popular_thumb_releaseDate.text = movie.releaseDate

            val posterUrl: String = POSTER_URL + movie.posterPath
            Glide.with(itemView.context)
                .load(posterUrl)
                .into(itemView.popular_thumb_image)
        }
    }
}