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
import com.bumptech.glide.Glide
import com.yunuscagliyan.sinemalog.MainActivity

import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.data.api.POSTER_BASE_URL
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBClient
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.model.MovieDetail
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.repository.MovieDetailRepository
import com.yunuscagliyan.sinemalog.databinding.FragmentMovieDetailBinding
import com.yunuscagliyan.sinemalog.ui.SharedPref
import com.yunuscagliyan.sinemalog.viewmodel.MovieDetailViewModel
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class MovieDetailFragment : Fragment() {

    private lateinit var binding:FragmentMovieDetailBinding
    private lateinit var viewModel:MovieDetailViewModel
    private lateinit var movieDetailRepository: MovieDetailRepository
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentMovieDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
        initToolbar()
        initUI()


    }

    private fun initUI() {
        val apiService: TheMovieDBInterface = TheMovieDBClient.getClients()
        movieDetailRepository=MovieDetailRepository(apiService)
        arguments!!.let {
            val safeArgs=MovieDetailFragmentArgs.fromBundle(it)
            val movieId=safeArgs.movieId
            viewModel=getViewModel(movieId)
        }
        viewModel.movieDetail.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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
            if(it==NetworkState.ERROR){
                binding.linearLayout.visibility=View.GONE
                binding.txtError.visibility=View.VISIBLE

            }
        })



    }

    fun bindUI( it: MovieDetail){
        binding.toolbar.title=it.title
        binding.movieTitle.text = it.title
        binding.movieTagline.text = it.tagline
        binding.movieRating.text=""+it.voteAverage

        var simpleDateFormat= SimpleDateFormat("yyyy-MM-dd")
        if(it.releaseDate!=null){
            var date= simpleDateFormat.parse(it.releaseDate)
            var dateFormat= DateFormat.getDateInstance(DateFormat.LONG)
            binding.movieReleaseDate.text=""+dateFormat.format(date)
        }else{
            binding.movieReleaseDate.text=""
        }
        binding.movieRuntime.text = it.runtime.toString() + " minutes"
        binding.movieOverview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        binding.movieBudget.text = formatCurrency.format(it.budget)
        binding.movieRevenue.text = formatCurrency.format(it.revenue)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(binding.ivMoviePoster)


    }
    private fun initToolbar() {
        (activity as MainActivity).setUpToolbar(binding.toolbar)
        var sharedPref = SharedPref(context!!)
        var statusColor=sharedPref.loadStatusBarColor()
        var window=activity!!.window
        window.statusBarColor=statusColor
    }
    private fun getViewModel(movieId:Int): MovieDetailViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailViewModel(movieDetailRepository,movieId) as T
            }
        })[MovieDetailViewModel::class.java]
    }

}
