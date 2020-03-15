package com.yunuscagliyan.sinemalog.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.adapter.HomeCatergoryAdapter
import com.yunuscagliyan.sinemalog.databinding.FragmentHomePageBinding
import com.yunuscagliyan.sinemalog.ui.SharedPref

/**
 * A simple [Fragment] subclass.
 */
class HomePageFragment : Fragment() {

    private lateinit var binding: FragmentHomePageBinding
    private lateinit var pagerAdapter: HomeCatergoryAdapter
    private lateinit var navController: NavController
    private var fragmentList=ArrayList<Fragment>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomePageBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
        setUpToolbar()
        //initViewPager()
    }

    private fun setUpToolbar() {
        (activity as MainActivity).setUpToolbar(binding.toolbar)
        var sharedPref = SharedPref(context!!)
        var themeColor = sharedPref.loadToolBarColor()
        var statusColor=sharedPref.loadStatusBarColor()
        binding.collapsingToolbar.setBackgroundColor(themeColor)
        var window=activity!!.window
        window.statusBarColor=statusColor
    }
    /*

    private fun initViewPager() {
        fragmentList.add(TrendingFragment())
        fragmentList.add(PopularFragment())
        fragmentList.add(TvSeriesFragment())
        fragmentList.add(TvShowFragment())


        var titleList= ArrayList<String>()
        titleList.add("Trending")
        titleList.add("Popular")
        titleList.add("TV Series")
        titleList.add("TV Shows")
        pagerAdapter= HomeCatergoryAdapter(fragmentList,titleList,requireActivity().supportFragmentManager)
        binding.viewPager.adapter=pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }

     */



}
