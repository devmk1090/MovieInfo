package com.devkproject.movieinfo.detail.credits

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.databinding.CrewItemBinding
import com.devkproject.movieinfo.model.TMDBCrew

class CrewRVAdapter (private val item: ArrayList<TMDBCrew>, private val context: Context): RecyclerView.Adapter<CrewRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CrewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position], context)
    }

    class ViewHolder(private val binding: CrewItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(crew: TMDBCrew?, context: Context) {
            binding.crewName.text = crew!!.name
            binding.crewJob.text = crew.job
            val profileUrl: String = POSTER_URL + crew.picture
            Glide.with(itemView.context)
                .load(profileUrl)
                .placeholder(R.drawable.ic_person_black_24dp)
                .into(binding.crewImage)
        }
    }
}