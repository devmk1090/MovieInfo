package com.devkproject.movieinfo.db

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.detail.DetailActivity
import kotlinx.android.synthetic.main.movie_item.view.*

//internal : 같은 모듈 안에서 잡근 가능
class FavoriteRVAdapter internal constructor(private val context: Context): RecyclerView.Adapter<FavoriteRVAdapter.FavoriteViewHolder>() {

    private var favorite = emptyList<Favorite>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return FavoriteViewHolder(v)
    }

    override fun getItemCount(): Int {
        return favorite.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favorite[position], context)
    }

    internal fun setFavorite(favorite: List<Favorite>) {
        this.favorite = favorite
        notifyDataSetChanged()
    }

    inner class FavoriteViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(favorite: Favorite, context: Context) {
            itemView.popular_thumb_title.text = favorite.title
            itemView.popular_thumb_releaseDate.text = favorite.release
            itemView.popular_rating.text = favorite.rating.toString()
            val posterUrl: String = POSTER_URL + favorite.poster
            Glide.with(itemView.context)
                .load(posterUrl)
                .into(itemView.popular_thumb_image)
            itemView.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("id", favorite.movieId)
                intent.putExtra("title", favorite.title)
                intent.putExtra("release", favorite.release)
                intent.putExtra("rating", favorite.rating)
                intent.putExtra("poster", favorite.poster)
                context.startActivity(intent)
            }
        }
    }
}