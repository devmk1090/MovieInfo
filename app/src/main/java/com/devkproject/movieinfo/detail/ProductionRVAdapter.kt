package com.devkproject.movieinfo.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.model.Production
import kotlinx.android.synthetic.main.production_item.view.*

class ProductionRVAdapter (private val item: ArrayList<Production>): RecyclerView.Adapter<ProductionRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.production_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(it: Production?) {
            val name = " [ " + it!!.name + " ] "
            itemView.production_list.text = name
        }
    }
}