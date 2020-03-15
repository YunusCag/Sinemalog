package com.yunuscagliyan.sinemalog.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.data.api.POSTER_BASE_URL
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBClient
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.model.SeriesDetail
import com.yunuscagliyan.sinemalog.data.repository.SeriesDetailRepository
import com.yunuscagliyan.sinemalog.databinding.FragmentSeriesDetailBinding
import com.yunuscagliyan.sinemalog.ui.SharedPref
import com.yunuscagliyan.sinemalog.viewmodel.SeriesDetailViewModel
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SeriesDetailFragment : Fragment() {

    private lateinit var binding:FragmentSeriesDetailBinding
    private lateinit var navController: NavController
    private lateinit var repository:SeriesDetailRepository
    private lateinit var viewModel:SeriesDetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSeriesDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
        initToolbar()
        initUI()
        

    }
    private fun initUI() {
        val apiService= TheMovieDBClient.getClients()
        repository= SeriesDetailRepository(apiService)
        arguments!!.let {
            val safeArgs=SeriesDetailFragmentArgs.fromBundle(it)
            val seriesId=safeArgs.seriesId
            viewModel=getViewModel(seriesId)

        }
        viewModel.seriesDetail.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            bindUI(it)

        })
        viewModel.networkState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it== NetworkState.LOADING){
                binding.progressBar.visibility=View.VISIBLE
                binding.linearLayout.visibility=View.GONE
            }else{
                binding.progressBar.visibility=View.GONE
                binding.linearLayout.visibility=View.VISIBLE
            }
            if(it== NetworkState.ERROR){
                binding.linearLayout.visibility=View.GONE
                binding.txtError.visibility=View.VISIBLE

            }
        })



    }
    fun bindUI( it: SeriesDetail){
        binding.toolbar.title=it.originalName
        binding.seriesTitle.text = it.originalName
        binding.seriesTagline.text = it.name
        binding.seriesRating.text=""+it.voteAverage

        var simpleDateFormat= SimpleDateFormat("yyyy-MM-dd")
        if(it.firstAirDate!=null){
            var date= simpleDateFormat.parse(it.firstAirDate)
            var dateFormat= DateFormat.getDateInstance(DateFormat.LONG)
            binding.seriesReleaseDate.text=""+dateFormat.format(date)
        }else{
            binding.seriesReleaseDate.text=""
        }
        if(it.lastAirDate!=null){
            var date= simpleDateFormat.parse(it.lastAirDate)
            var dateFormat= DateFormat.getDateInstance(DateFormat.LONG)
            binding.seriesEndDate.text=""+dateFormat.format(date)
        }else{
            binding.seriesEndDate.text=""
        }
        binding.seriesType.text = ""+it.type
        binding.seriesOverview.text = it.overview
        binding.seriesStatus.text=""+it.status
        binding.seriesPopularity.text=""+it.popularity

        binding.seriesEpisodes.text = ""+it.numberOfEpisodes
        binding.seriesSeasons.text = ""+it.seasons.size

        val seriesPosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(context!!)
            .load(seriesPosterURL)
            .into(binding.ivSeriesPoster)


    }
    fun initToolbar(){
        NavigationUI.setupWithNavController(binding.toolbar,navController)
        var sharedPref = SharedPref(context!!)
        var statusColor=sharedPref.loadStatusBarColor()
        var window=activity!!.window
        window.statusBarColor=statusColor
    }
    private fun getViewModel(seriesId:Int): SeriesDetailViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SeriesDetailViewModel(repository,seriesId) as T
            }
        })[SeriesDetailViewModel::class.java]
    }

}
