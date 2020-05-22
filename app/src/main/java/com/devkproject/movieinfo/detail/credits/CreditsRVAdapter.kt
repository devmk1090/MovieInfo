package com.devkproject.movieinfo.detail.credits

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.detail.person.PersonActivity
import com.devkproject.movieinfo.model.TMDBCast
import kotlinx.android.synthetic.main.credits_item.view.*

class CreditsRVAdapter (private val item: ArrayList<TMDBCast>, private val context: Context): RecyclerView.Adapter<CreditsRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.credits_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position], context)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(cast: TMDBCast?, context: Context) {
            itemView.credits_character.text = cast!!.character
            itemView.credits_name.text = cast.name

            val profileUrl: String = POSTER_URL + cast.picture
            Glide.with(itemView.context)
                .load(profileUrl)
                .placeholder(R.drawable.ic_person_black_24dp)
                .into(itemView.credits_image)
            itemView.credits_image.setOnClickListener {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val intent = Intent(context, PersonActivity::class.java)
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity,
                        itemView.credits_image, itemView.credits_image.transitionName)
                    intent.putExtra("id", cast.id)
                    intent.putExtra("picture", cast.picture)
                    intent.putExtra("name", cast.name)
                    context.startActivity(intent, options.toBundle())
                    Log.d("PERSON", cast.id.toString())
                } else {
                    val intent = Intent(context, PersonActivity::class.java)
                    intent.putExtra("id", cast.id)
                    intent.putExtra("picture", cast.picture)
                    intent.putExtra("name", cast.name)
                    context.startActivity(intent)
                }
            }
        }
    }
}

