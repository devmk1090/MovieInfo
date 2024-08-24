package com.devkproject.movieinfo.detail.person

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.databinding.MovieItemBinding
import com.devkproject.movieinfo.detail.DetailActivity
import com.devkproject.movieinfo.model.TMDBPersonCast

class PersonRVAdapter(private val item: ArrayList<TMDBPersonCast>, private val context: Context): RecyclerView.Adapter<PersonRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position], context)
    }

    class ViewHolder(private val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: TMDBPersonCast?, context: Context) {
            binding.popularThumbTitle.text = cast!!.title
            binding.popularThumbReleaseDate.text = cast.releaseDate
            binding.popularRating.text = cast.rating.toString()

            val profileUrl: String = POSTER_URL + cast.posterPath
            Glide.with(itemView.context)
                .load(profileUrl)
                .into(binding.popularThumbImage)
            itemView.setOnClickListener {
                Intent(context, DetailActivity::class.java).apply {
                    putExtra("id", cast.id)
                    putExtra("title", cast.title)
                    putExtra("release", cast.releaseDate)
                    putExtra("rating", cast.rating)
                    putExtra("poster", cast.posterPath)
                    context.startActivity(this)
                }
            }
        }
    }
}