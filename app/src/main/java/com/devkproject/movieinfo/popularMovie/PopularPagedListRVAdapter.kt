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

class PopularPagedListRVAdapter(val context: Context)
    : PagedListAdapter<TMDBThumb, RecyclerView.ViewHolder>(TMDBDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.popular_item, parent, false)
        return PopularViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PopularViewHolder).bind(getItem(position), context)
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