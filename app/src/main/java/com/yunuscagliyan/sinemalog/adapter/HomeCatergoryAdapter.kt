package com.yunuscagliyan.sinemalog.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeCatergoryAdapter
    (private val fragmentList:List<Fragment>,
     private val titleList:ArrayList<String>, fm:FragmentManager)
    : FragmentStatePagerAdapter(fm,FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var mFragmentList:ArrayList<Fragment> = fragmentList as ArrayList<Fragment>

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return titleList.size
    }



}