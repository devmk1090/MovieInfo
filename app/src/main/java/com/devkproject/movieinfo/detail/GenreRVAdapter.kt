package com.devkproject.movieinfo.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.model.Genres
import kotlinx.android.synthetic.main.genre_item.view.*

class GenreRVAdapter (private val item: ArrayList<Genres>): RecyclerView.Adapter<GenreRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.genre_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position])
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(it: Genres?) {
            val genre = " [ " + it!!.name + " ] "
            itemView.genre_list.text = genre
        }
    }
}