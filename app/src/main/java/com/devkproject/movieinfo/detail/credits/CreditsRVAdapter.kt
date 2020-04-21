package com.devkproject.movieinfo.detail.credits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.model.TMDBCast
import kotlinx.android.synthetic.main.credits_item.view.*

class CreditsRVAdapter (val item: ArrayList<TMDBCast>): RecyclerView.Adapter<CreditsRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.credits_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(it: TMDBCast?) {
            itemView.credits_character.text = it!!.character
            itemView.credits_name.text = it.name

            val profileUrl: String = POSTER_URL + it.picture
            Glide.with(itemView.context)
                .load(profileUrl)
                .into(itemView.credits_image)
        }
    }
}