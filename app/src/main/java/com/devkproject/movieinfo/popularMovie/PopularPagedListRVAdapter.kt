package com.devkproject.movieinfo.popularMovie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.SelectedMovie.SelectedActivity
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.model.TMDBThumb
import kotlinx.android.synthetic.main.popular_item.view.*
import kotlinx.android.synthetic.main.toprated_item.view.*

class PopularPagedListRVAdapter(private val context: Context)
    : PagedListAdapter<TMDBThumb, RecyclerView.ViewHolder>(TMDBDiffCallback()) {

    val POPULAR_MOVIE = 1
    val TOP_RATED_MOVIE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val v: View

        return if(viewType == POPULAR_MOVIE) {
            v = layoutInflater.inflate(R.layout.popular_item, parent, false)
            PopularViewHolder(v)
        } else {
            v = layoutInflater.inflate(R.layout.toprated_item, parent, false)
            TopRatedViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == POPULAR_MOVIE) {
            (holder as PopularViewHolder).bind(getItem(position), context)
        } else {
            (holder as TopRatedViewHolder).bind2(getItem(position), context)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            MainActivity.VIEW_TYPE -> POPULAR_MOVIE
            else -> TOP_RATED_MOVIE
        }
    }

    class PopularViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(movie: TMDBThumb?, context: Context) {
            itemView.popular_thumb_title.text = movie!!.title
            itemView.popular_thumb_releaseDate.text = movie.releaseDate

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

    class TopRatedViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind2(movie: TMDBThumb?, context: Context) {
            itemView.topRated_title.text = movie!!.title
            itemView.topRated_releaseDate.text = movie.releaseDate
            itemView.topRated_rating.text = movie.rating.toString()

            val posterUrl: String = POSTER_URL + movie.posterPath
            Glide.with(itemView.context)
                .load(posterUrl)
                .into(itemView.topRated_image)
        }
    }

    //DiffUtill.Callback 은 추상클래스. 두 목록간의 차이점을 찾고 업데이트 되어야 할 목록을 반환해줌
    //RecyclerView Adapter 에 대한 업데이트를 알리는데 사용됨
    class TMDBDiffCallback: DiffUtil.ItemCallback<TMDBThumb>() {
        override fun areItemsTheSame(oldItem: TMDBThumb, newItem: TMDBThumb): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TMDBThumb, newItem: TMDBThumb): Boolean {
            return oldItem == newItem
        }

    }
}