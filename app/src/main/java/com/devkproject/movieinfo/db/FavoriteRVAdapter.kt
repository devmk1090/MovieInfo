package com.devkproject.movieinfo.db

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devkproject.movieinfo.R

//internal : 같은 모듈 안에서 잡근 가능
class FavoriteRVAdapter internal constructor(context: Context): RecyclerView.Adapter<FavoriteRVAdapter.FavoriteViewHolder>() {

    private var favorite = emptyList<Favorite>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return FavoriteViewHolder(v)
    }

    override fun getItemCount(): Int {
        return favorite.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val current = favorite[position]
        holder.title.text = current.movieId.toString()
    }

    internal fun setFavorite(favorite: List<Favorite>) {
        this.favorite = favorite
        notifyDataSetChanged()
    }

    inner class FavoriteViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title = itemView.findViewById<TextView>(R.id.popular_thumb_title)
    }
}