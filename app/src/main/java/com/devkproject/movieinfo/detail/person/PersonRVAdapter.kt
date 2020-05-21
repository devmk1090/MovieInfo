package com.devkproject.movieinfo.detail.person

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
import com.devkproject.movieinfo.model.TMDBPersonCast
import kotlinx.android.synthetic.main.movie_item.view.*

class PersonRVAdapter(private val item: ArrayList<TMDBPersonCast>, private val context: Context): RecyclerView.Adapter<PersonRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position], context)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(cast: TMDBPersonCast?, context: Context) {
            itemView.popular_thumb_title.text = cast!!.title
            itemView.popular_thumb_releaseDate.text = cast.releaseDate
            itemView.popular_rating.text = cast.rating.toString()

            val profileUrl: String = POSTER_URL + cast.posterPath
            Glide.with(itemView.context)
                .load(profileUrl)
                .into(itemView.popular_thumb_image)
            itemView.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("id", cast.id)
                intent.putExtra("title", cast.title)
                intent.putExtra("release", cast.releaseDate)
                intent.putExtra("rating", cast.rating)
                intent.putExtra("poster", cast.posterPath)
                context.startActivity(intent)
            }
        }
    }
}