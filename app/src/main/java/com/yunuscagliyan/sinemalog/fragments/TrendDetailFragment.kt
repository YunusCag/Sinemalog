package com.yunuscagliyan.sinemalog.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.yunuscagliyan.sinemalog.data.api.POSTER_BASE_URL
import com.yunuscagliyan.sinemalog.data.model.Trend
import com.yunuscagliyan.sinemalog.databinding.FragmentTrendDetailBinding
import com.yunuscagliyan.sinemalog.ui.SharedPref
import java.sql.Date
import java.text.DateFormat
import java.text.SimpleDateFormat


/**
 * A simple [Fragment] subclass.
 */
class TrendDetailFragment : Fragment() {

    private lateinit var binding:FragmentTrendDetailBinding
    private lateinit var navController: NavController
    private lateinit var trend: Trend
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentTrendDetailBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
        initToolbar()
        arguments!!.let {
            val safeArgs=TrendDetailFragmentArgs.fromBundle(it!!)
            trend=safeArgs.trend
            val trendPosterURL= POSTER_BASE_URL+trend.backdropPath
            Glide.with(context!!)
                .load(trendPosterURL)
                .into(binding.imageView)
        }
        bindUI()
        initAdMob()



    }

    private fun initAdMob() {
        val adRequest = AdRequest.Builder()
            .build()
        adRequest.isTestDevice(context)
        binding.rowAdItem.loadAd(adRequest)
    }


    private fun bindUI(){
        binding.movieRuntime.text=trend.name
        binding.trendOverview.text=""+trend.overview
        binding.trendOriginalName.text=trend.originalName
        binding.trendOriginalTitle.text=trend.originalTitle

        var simpleDateFormat=SimpleDateFormat("yyyy-MM-dd")
        if(trend.releaseDate!=null){
            var date= simpleDateFormat.parse(trend.releaseDate)
            var dateFormat=DateFormat.getDateInstance(DateFormat.LONG)
            binding.trendReleaseDate.text=""+dateFormat.format(date)
        }else{
            binding.trendReleaseDate.text=""
        }

        binding.trendRating.text=""+trend.voteAverage
        binding.trendVoteAverage.text=""+trend.voteAverage
        binding.trendVoteCount.text=""+trend.voteCount

        binding.toolbar.title = trend.title
    }


    fun initToolbar(){
        NavigationUI.setupWithNavController(binding.toolbar,navController)
        var sharedPref = SharedPref(context!!)
        var statusColor=sharedPref.loadStatusBarColor()
        var window=activity!!.window
        window.statusBarColor=statusColor
    }



}
