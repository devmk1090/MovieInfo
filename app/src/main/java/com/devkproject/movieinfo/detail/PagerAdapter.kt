package com.devkproject.movieinfo.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.devkproject.movieinfo.detail.credits.CreditsFragment

class PagerAdapter (fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> InfoFragment()
            1 -> SynopsisFragment()
            else -> CreditsFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "정보"
            1 -> "줄거리"
            else -> "출연배우"
        }
    }
}