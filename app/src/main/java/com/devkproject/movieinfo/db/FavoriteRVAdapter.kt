package com.devkproject.movieinfo.db

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.databinding.MovieItemBinding
import com.devkproject.movieinfo.detail.DetailActivity

//internal : 같은 모듈 안에서 잡근 가능
class FavoriteRVAdapter internal constructor(private val context: Context): RecyclerView.Adapter<FavoriteRVAdapter.FavoriteViewHolder>() {

    private var favorite = emptyList<Favorite>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
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

    inner class FavoriteViewHolder(private val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite, context: Context) {
            binding.popularThumbTitle.text = favorite.title
            binding.popularThumbReleaseDate.text = favorite.release
            binding.popularRating.text = favorite.rating.toString()
            val posterUrl: String = POSTER_URL + favorite.poster
            Glide.with(itemView.context)
                .load(posterUrl)
                .into(binding.popularThumbImage)
            itemView.setOnClickListener {
                Intent(context, DetailActivity::class.java).apply {
                    putExtra("id", favorite.movieId)
                    putExtra("title", favorite.title)
                    putExtra("release", favorite.release)
                    putExtra("rating", favorite.rating)
                    putExtra("poster", favorite.poster)
                    context.startActivity(this)
                }
            }
        }
    }
}