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
import com.devkproject.movieinfo.databinding.MovieItemBinding
import com.devkproject.movieinfo.databinding.NetworkItemBinding
import com.devkproject.movieinfo.model.TMDBThumb

class PagedListRVAdapter(private val context: Context)
    : PagedListAdapter<TMDBThumb, RecyclerView.ViewHolder>(TMDBDiffCallback()) {

    val MOVIE_TYPE = 1
    val NETWORK_TYPE = 2
    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)

        return if(viewType == MOVIE_TYPE) {
            val binding = MovieItemBinding.inflate(layoutInflater, parent, false)
            PopularViewHolder(binding)
        }
        else {
            val binding = NetworkItemBinding.inflate(layoutInflater, parent, false)
            NetworkViewHolder(binding)
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

    class PopularViewHolder(private val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: TMDBThumb?, context: Context) {
            binding.popularThumbTitle.text = movie!!.title
            binding.popularThumbReleaseDate.text = movie.releaseDate
            binding.popularRating.text = movie.rating.toString()

            val posterUrl: String = POSTER_URL + movie.posterPath
            Glide.with(itemView.context)
                .load(posterUrl)
                .into(binding.popularThumbImage)

            itemView.setOnClickListener {
                Intent(context, DetailActivity::class.java).apply {
                    putExtra("id", movie.id)
                    putExtra("title", movie.title)
                    putExtra("release", movie.releaseDate)
                    putExtra("rating", movie.rating)
                    putExtra("poster", movie.posterPath)
                    context.startActivity(this)
                }
            }
        }
    }

    class NetworkViewHolder(private val binding: NetworkItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(networkState: NetworkState?) {
            if(networkState != null && networkState == NetworkState.LOADING) {
                binding.progressBarItem.visibility = View.VISIBLE
            }
            else {
                binding.progressBarItem.visibility = View.GONE
            }
            if(networkState != null && networkState == NetworkState.ERROR) {
                binding.errorMsgItem.visibility = View.VISIBLE
                binding.errorMsgItem.text = networkState.message
            }
            else if(networkState != null && networkState == NetworkState.ENDOFLIST) {
                binding.errorMsgItem.visibility = View.VISIBLE
                binding.errorMsgItem.text = networkState.message
            }
            else {
                binding.errorMsgItem.visibility = View.GONE
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