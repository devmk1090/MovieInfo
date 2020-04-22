package com.devkproject.movieinfo.detail.credits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.model.TMDBCrew
import kotlinx.android.synthetic.main.crew_item.view.*

class CrewRVAdapter (private val item: ArrayList<TMDBCrew>): RecyclerView.Adapter<CrewRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.crew_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(it: TMDBCrew?) {
            itemView.crew_name.text = it!!.name
                itemView.crew_job.text = it.job
            val profileUrl: String = POSTER_URL + it.picture
            Glide.with(itemView.context)
                .load(profileUrl)
                .into(itemView.crew_image)
        }
    }
}