package com.devkproject.movieinfo.detail.person

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.model.TMDBPersonCast
import kotlinx.android.synthetic.main.movie_item.view.*

class PersonRVAdapter(private val item: ArrayList<TMDBPersonCast>): RecyclerView.Adapter<PersonRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(it: TMDBPersonCast?) {
            itemView.popular_thumb_title.text = it!!.title
            itemView.popular_thumb_releaseDate.text = it.releaseDate
            itemView.popular_rating.text = it.rating.toString()

            val profileUrl: String = POSTER_URL + it.posterPath
            Glide.with(itemView.context)
                .load(profileUrl)
                .into(itemView.popular_thumb_image)
        }
    }
}