package com.devkproject.movieinfo

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.detail.DetailActivity
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.model.TMDBThumb
import kotlinx.android.synthetic.main.movie_item.view.*
import kotlinx.android.synthetic.main.network_item.view.*

class PagedListRVAdapter(private val context: Context)
    : PagedListAdapter<TMDBThumb, RecyclerView.ViewHolder>(TMDBDiffCallback()) {

    val MOVIE_TYPE = 1
    val NETWORK_TYPE = 2
    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val v: View

        return if(viewType == MOVIE_TYPE) {
            v = layoutInflater.inflate(R.layout.movie_item, parent, false)
            PopularViewHolder(v)
        }
        else {
            v = layoutInflater.inflate(R.layout.network_item, parent, false)
            NetworkViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == MOVIE_TYPE) {
            (holder as PopularViewHolder).bind(getItem(position), context)
        }
        else {
            (holder as NetworkViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if(hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRow() && position == itemCount - 1) {
            NETWORK_TYPE
        }
        else {
            MOVIE_TYPE
        }
    }

    class PopularViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(movie: TMDBThumb?, context: Context) {
            itemView.popular_thumb_title.text = movie!!.title
            itemView.popular_thumb_releaseDate.text = movie.releaseDate
            itemView.popular_rating.text = movie.rating.toString()

            val posterUrl: String = POSTER_URL + movie.posterPath
            Glide.with(itemView.context)
                .load(posterUrl)
                .into(itemView.popular_thumb_image)

            itemView.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("id", movie.id)
                intent.putExtra("title", movie.title)
                intent.putExtra("release", movie.releaseDate)
                intent.putExtra("rating", movie.rating)
                intent.putExtra("poster", movie.posterPath)
                context.startActivity(intent)
            }
        }
    }

    class NetworkViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState?) {
            if(networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE
            }
            else {
                itemView.progress_bar_item.visibility = View.GONE
            }
            if(networkState != null && networkState == NetworkState.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE
                itemView.error_msg_item.text = networkState.message
            }
            else if(networkState != null && networkState == NetworkState.ENDOFLIST) {
                itemView.error_msg_item.visibility = View.VISIBLE
                itemView.error_msg_item.text = networkState.message
            }
            else {
                itemView.error_msg_item.visibility = View.GONE
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState: NetworkState? = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow: Boolean = hasExtraRow()

        if(hadExtraRow != hasExtraRow) {
            if(hadExtraRow) {                           //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount()) //remove the progressbar at the end
            }
            else {                                      //hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())//add the progressbar at the end
            }
        } else if(hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow is true
            notifyItemChanged(itemCount - 1)
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