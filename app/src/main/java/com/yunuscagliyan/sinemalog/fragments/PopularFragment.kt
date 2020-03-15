package com.yunuscagliyan.sinemalog.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.adapter.PopularMoviePagedListAdapter
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBClient
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.repository.MoviePagedListRepository

import com.yunuscagliyan.sinemalog.databinding.FragmentPopularBinding
import com.yunuscagliyan.sinemalog.eventbus.MovieDetailMessage
import com.yunuscagliyan.sinemalog.eventbus.TrendDetailMessage
import com.yunuscagliyan.sinemalog.ui.SharedPref
import com.yunuscagliyan.sinemalog.ui.Utility
import com.yunuscagliyan.sinemalog.viewmodel.PopularViewModel
import com.yunuscagliyan.sinemalog.viewmodel.TrendingViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * A simple [Fragment] subclass.
 */
class PopularFragment : Fragment() {

    private lateinit var binding:FragmentPopularBinding
    private lateinit var navController: NavController

    private lateinit var viewModel: PopularViewModel
    private lateinit var movieRepository:MoviePagedListRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentPopularBinding.inflate(inflater,container,false)


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        initToolbar()
        initRecyclerView()


    }

    private fun initRecyclerView() {
        val apiService=TheMovieDBClient.getClients()
        movieRepository=MoviePagedListRepository(apiService)
        viewModel=getViewModel()
        val columnCount= Utility.calculateNoOfColumns(context!!,120f)
        val movieAdapter=PopularMoviePagedListAdapter(context!!,columnCount)
        val gridLayoutManager=GridLayoutManager(context!!,columnCount)
        gridLayoutManager.spanSizeLookup=object :GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType=movieAdapter.getItemViewType(position)
                return if(viewType==movieAdapter.MOVIE_VIEW_TYPE){
                    1
                }else{
                    columnCount
                }
            }

        }
        binding.refreshButton.setOnClickListener {
            movieRepository.movieDataSourceFactory.movieDataSource
                .invalidate()
        }
        binding.rvMovieList.layoutManager=gridLayoutManager
        binding.rvMovieList.adapter=movieAdapter
        viewModel.moviePagedList.observe(viewLifecycleOwner, Observer {
            movieAdapter.submitList(it!!)
        })
        viewModel.neworkState.observe(viewLifecycleOwner, Observer {
            binding.progressBarPopular.visibility=if(viewModel.listIsEmpty()&&it== NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.textErrorPopular.visibility=if(viewModel.listIsEmpty()&&it== NetworkState.ERROR) View.VISIBLE else View.GONE
            binding.refreshButton.visibility=if(viewModel.listIsEmpty()&&it== NetworkState.ERROR) View.VISIBLE else View.GONE

            if(!viewModel.listIsEmpty()){
                movieAdapter.setNetworkState(it)
            }
        })


    }

    private fun initToolbar() {
        (activity as MainActivity).setUpToolbar(binding.toolbar)
        var sharedPref = SharedPref(context!!)
        var themeColor = sharedPref.loadToolBarColor()
        var statusColor=sharedPref.loadStatusBarColor()
        binding.toolbar.setBackgroundColor(themeColor)
        var window=activity!!.window
        window.statusBarColor=statusColor
    }

    private fun getViewModel(): PopularViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PopularViewModel(movieRepository) as T
            }
        })[PopularViewModel::class.java]
    }
    @Subscribe(sticky = false,threadMode = ThreadMode.MAIN)
    fun navigateMovieDetail(movie: MovieDetailMessage){
        val nextDetail=PopularFragmentDirections.detailAction(movie.movieId)
        navController.navigate(nextDetail)
        Log.e("PopularFragment","DetailFragment Navigation")

    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

}
