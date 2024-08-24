package com.devkproject.movieinfo.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devkproject.movieinfo.databinding.GenreItemBinding
import com.devkproject.movieinfo.model.Genres

class GenreRVAdapter (private val item: ArrayList<Genres>): RecyclerView.Adapter<GenreRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GenreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position])
    }


    class ViewHolder(private val binding: GenreItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(it: Genres?) {
            val genre = " [ " + it!!.name + " ] "
            binding.genreList.text = genre
        }
    }
}