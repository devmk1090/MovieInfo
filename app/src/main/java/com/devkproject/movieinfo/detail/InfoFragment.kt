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

import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.TMDBClient
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.model.TMDBDetail
import kotlinx.android.synthetic.main.fragment_info.*
import java.text.DecimalFormat

/**
 * A simple [Fragment] subclass.
 */
class InfoFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var selectedMovieRepository: DetailRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val movieId: Int = activity!!.intent.getIntExtra("id", 1)
        val apiService: TMDBInterface = TMDBClient.getClient()
        selectedMovieRepository = DetailRepository(apiService)

        viewModel = getViewModel(movieId)
        viewModel.selectedMovie.observe(viewLifecycleOwner, Observer {
            bindUI(it)
        })

        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    private fun bindUI(it: TMDBDetail) {
        val decimalFormat = DecimalFormat("###,###")
        val decimalBudget = decimalFormat.format(it.budget) + " $"
        val decimalRevenue = decimalFormat.format(it.revenue) + " $"
        val runtime = it.runtime.toString() + " 분"

        selected_movie_release.text = it.releaseDate
        selected_movie_rating.text = it.rating.toString()
        selected_movie_vote_count.text = it.vote_count.toString()
        selected_movie_budget.text = decimalBudget
        selected_movie_revenue.text = decimalRevenue
        selected_movie_overview.text = it.overview
        selected_movie_runtime.text = runtime

    }

        private fun getViewModel(movieId: Int): DetailViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DetailViewModel(
                    selectedMovieRepository,
                    movieId
                ) as T
            }
        })[DetailViewModel::class.java]
    }
}
