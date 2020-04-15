package com.devkproject.movieinfo.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.model.TMDBThumb
import com.devkproject.movieinfo.selected.SelectedActivity
import kotlinx.android.synthetic.main.popular_item.view.*

class SearchRVAdapter(val searchList: ArrayList<TMDBThumb>, private val context: Context): RecyclerView.Adapter<SearchRVAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.popular_item, parent, false)
        return SearchViewHolder(v)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(searchList[position], context)
    }


    class SearchViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(movie: TMDBThumb?, context: Context) {
            itemView.popular_thumb_title.text = movie!!.title
            itemView.popular_thumb_releaseDate.text = movie.releaseDate
            itemView.popular_rating.text = movie.rating.toString()

            val posterUrl: String = POSTER_URL + movie.posterPath
            Glide.with(itemView.context)
                .load(posterUrl)
                .into(itemView.popular_thumb_image)

            itemView.setOnClickListener {
                val intent = Intent(context, SelectedActivity::class.java)
                intent.putExtra("id", movie.id)
                context.startActivity(intent)
            }
        }
    }
}