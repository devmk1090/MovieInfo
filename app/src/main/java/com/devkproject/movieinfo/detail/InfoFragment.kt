package com.devkproject.movieinfo.detail


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.TMDBClient
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.Genres
import com.devkproject.movieinfo.model.Production
import com.devkproject.movieinfo.model.TMDBDetail
import kotlinx.android.synthetic.main.fragment_info.*
import java.text.DecimalFormat

/**
 * A simple [Fragment] subclass.
 */
class InfoFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var detailRepository: DetailRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val movieId: Int = activity!!.intent.getIntExtra("id", 1)
        val apiService: TMDBInterface = TMDBClient.getClient()
        detailRepository = DetailRepository(apiService)

        viewModel = getViewModel(movieId)
        viewModel.selectedMovie.observe(viewLifecycleOwner, Observer {
            bindUI(it)
            setGenreRVAdapter(it.genres)
            setProductionRVAdapter(it.production_countries)
        })
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    private fun bindUI(it: TMDBDetail) {
        val decimalFormat = DecimalFormat("###,###")
        val decimalBudget = decimalFormat.format(it.budget) + " $"
        val decimalRevenue = decimalFormat.format(it.revenue) + " $"
        val runtime = it.runtime.toString() + " 분"

        info_release.text = it.releaseDate
        info_rating.text = it.rating.toString()
        info_vote_count.text = it.vote_count.toString()
        info_budget.text = decimalBudget
        info_revenue.text = decimalRevenue
        info_runtime.text = runtime
    }

    private fun setGenreRVAdapter(item: ArrayList<Genres>) {
        val genreRVAdapter = GenreRVAdapter(item)
        genre_recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        genre_recyclerView.setHasFixedSize(true)
        genre_recyclerView.adapter = genreRVAdapter
    }

    private fun setProductionRVAdapter(item: ArrayList<Production>) {
        val productionRVAdapter = ProductionRVAdapter(item)
        production_recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        production_recyclerView.setHasFixedSize(true)
        production_recyclerView.adapter = productionRVAdapter
    }

    private fun getViewModel(movieId: Int): DetailViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DetailViewModel(detailRepository, movieId) as T
            }
        })[DetailViewModel::class.java]
    }
}
