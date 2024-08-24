package com.devkproject.movieinfo.detail.credits

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.databinding.CreditsItemBinding
import com.devkproject.movieinfo.detail.person.PersonActivity
import com.devkproject.movieinfo.model.TMDBCast

class CreditsRVAdapter (private val item: ArrayList<TMDBCast>, private val context: Context): RecyclerView.Adapter<CreditsRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CreditsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position], context)
    }

    class ViewHolder(private val binding: CreditsItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: TMDBCast?, context: Context) {
            binding.creditsCharacter.text = cast!!.character
            binding.creditsName.text = cast.name

            val profileUrl: String = POSTER_URL + cast.picture
            Glide.with(itemView.context)
                .load(profileUrl)
                .placeholder(R.drawable.ic_person_black_24dp)
                .into(binding.creditsImage)
            binding.creditsImage.setOnClickListener {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity,
                        binding.creditsImage, binding.creditsImage.transitionName)
                    Intent(context, PersonActivity::class.java).apply {
                        putExtra("id", cast.id)
                        putExtra("picture", cast.picture)
                        putExtra("name", cast.name)
                        context.startActivity(this, options.toBundle())
                    }
                } else {
                    Intent(context, PersonActivity::class.java).apply {
                        putExtra("id", cast.id)
                        putExtra("picture", cast.picture)
                        putExtra("name", cast.name)
                        context.startActivity(this)
                    }
                }
            }
        }
    }
}

