package com.devkproject.movieinfo.detail.person

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.api.TMDBClient
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.databinding.ActivityPersonBinding
import com.devkproject.movieinfo.model.TMDBPersonCast
import kotlinx.android.synthetic.main.activity_person.*

class PersonActivity : AppCompatActivity() {

    private lateinit var personRepository: PersonRepository
    private lateinit var personViewModel: PersonViewModel
    private lateinit var binding: ActivityPersonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_person)

        val apiService: TMDBInterface = TMDBClient.getClient()
        val personId: Int = intent.getIntExtra("id", 0)
        val picture = intent.getStringExtra("picture")

        val profilePIC: String = POSTER_URL + picture
        Glide.with(this)
            .load(profilePIC)
            .into(person_profile)

        personRepository = PersonRepository(apiService)
        personViewModel = ViewModelProvider(this, PersonViewModel.PersonViewModelFactory(personRepository, personId))
            .get(PersonViewModel::class.java)
        personViewModel.getPerson.observe(this, Observer {
            setPersonCast(it.cast)
        })
    }


    private fun setPersonCast(item: ArrayList<TMDBPersonCast>) {
        val actorCreditsRVAdapter = PersonRVAdapter(item)
        person_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        person_recyclerView.setHasFixedSize(true)
        person_recyclerView.adapter = actorCreditsRVAdapter
    }
}
