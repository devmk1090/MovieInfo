package com.devkproject.movieinfo.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devkproject.movieinfo.databinding.ProductionItemBinding
import com.devkproject.movieinfo.model.Production

class ProductionRVAdapter (private val item: ArrayList<Production>): RecyclerView.Adapter<ProductionRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(item[position])
    }

    class ViewHolder(private val binding: ProductionItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(it: Production?) {
            val name = " [ " + it!!.name + " ] "
            binding.productionList.text = name
        }
    }
}