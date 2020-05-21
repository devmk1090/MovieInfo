package com.devkproject.movieinfo.detail.person

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.devkproject.movieinfo.R
import com.devkproject.movieinfo.api.POSTER_URL
import com.devkproject.movieinfo.api.TMDBClient
import com.devkproject.movieinfo.api.TMDBInterface
import com.devkproject.movieinfo.databinding.ActivityPersonBinding
import com.devkproject.movieinfo.model.TMDBPersonCast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_person.*

class PersonActivity : AppCompatActivity() {

    private lateinit var personRepository: PersonRepository
    private lateinit var personViewModel: PersonViewModel
    private lateinit var binding: ActivityPersonBinding

    private lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_person)

        val apiService: TMDBInterface = TMDBClient.getClient()
        val personId: Int = intent.getIntExtra("id", 0)
        val picture = intent.getStringExtra("picture")
        val name = intent.getStringExtra("name")

        MobileAds.initialize(this) {}
        mAdView = this.findViewById(R.id.adView_person)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val toolbar: Toolbar = findViewById(R.id.person_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        supportActionBar!!.title = name

        val profilePIC: String = POSTER_URL + picture
        Glide.with(this)
            .load(profilePIC)
            .placeholder(R.drawable.ic_person_black_24dp)
            .into(person_profile)
//        person_name.text = name

        personRepository = PersonRepository(apiService)
        personViewModel = ViewModelProvider(this, PersonViewModel.PersonViewModelFactory(personRepository, personId))
            .get(PersonViewModel::class.java)
        personViewModel.getPerson.observe(this, Observer {
            setPersonCast(it.cast)
        })
    }

    private fun setPersonCast(item: ArrayList<TMDBPersonCast>) {
        val actorCreditsRVAdapter = PersonRVAdapter(item, this)
        val gridLayoutManager = GridLayoutManager(this, 3)
        person_recyclerView.layoutManager = gridLayoutManager
        person_recyclerView.setHasFixedSize(true)
        person_recyclerView.adapter = actorCreditsRVAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
