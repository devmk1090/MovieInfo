//package com.devkproject.movieinfo.detail
//
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentPagerAdapter
//import com.devkproject.movieinfo.detail.credits.CreditsFragment
//import com.devkproject.movieinfo.detail.credits.CrewFragment
//
//class PagerAdapter (fm: FragmentManager): FragmentPagerAdapter(fm) {
//
//    override fun getItem(position: Int): Fragment {
//        return when(position) {
//            0 -> CreditsFragment()
//            else -> CrewFragment()
//        }
//    }
//
//    override fun getCount(): Int {
//        return 2
//    }
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        return when(position) {
//            0 -> "출연"
//            else -> "제작진"
//        }
//    }
//}