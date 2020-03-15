package com.yunuscagliyan.sinemalog.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.yunuscagliyan.sinemalog.MainActivity
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.adapter.TrendPagedListAdapter
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBClient
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.repository.TrendingPagedListRepository
import com.yunuscagliyan.sinemalog.databinding.FragmentTrendingBinding
import com.yunuscagliyan.sinemalog.eventbus.TrendDetailMessage
import com.yunuscagliyan.sinemalog.ui.SharedPref
import com.yunuscagliyan.sinemalog.ui.Utility
import com.yunuscagliyan.sinemalog.viewmodel.TrendingViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * A simple [Fragment] subclass.
 */
class TrendingFragment : Fragment() {


    private lateinit var binding: FragmentTrendingBinding
    private lateinit var viewModel:TrendingViewModel
    private lateinit var trendRepository:TrendingPagedListRepository
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentTrendingBinding.inflate(inflater,container,false)

        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
        initToolbar()
        initRecyclerView()


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

    private fun initRecyclerView() {
        val apiService=TheMovieDBClient.getClients()
        trendRepository= TrendingPagedListRepository(apiService)
        viewModel=getViewModel()

        val columnCount= Utility.calculateNoOfColumns(context!!,120f)
        val trendAdapter=TrendPagedListAdapter(context!!,columnCount)
        val gridLayoutManager=GridLayoutManager(context!!,columnCount)

        gridLayoutManager.spanSizeLookup=object :GridLayoutManager.SpanSizeLookup(){

            override fun getSpanSize(position: Int): Int {
                val viewType=trendAdapter.getItemViewType(position)

                return if(viewType==trendAdapter.TREND_VIEW_TYPE){
                    1
                }else{
                    columnCount
                }
            }

        }
        binding.refreshButton.setOnClickListener {
            trendRepository.trendingDataSourceFactory.trendingDataSource.invalidate()
        }
        binding.rvMovieList.layoutManager=gridLayoutManager
        binding.rvMovieList.setHasFixedSize(true)
        binding.rvMovieList.adapter=trendAdapter
        viewModel.trendingPagedList.observe(viewLifecycleOwner, Observer {
            trendAdapter.submitList(it)
        })
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            binding.progressBarPopular.visibility=if(viewModel.listIsEmpty()&&it== NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.textErrorPopular.visibility=if(viewModel.listIsEmpty()&&it== NetworkState.ERROR) View.VISIBLE else View.GONE
            binding.refreshButton.visibility=if(viewModel.listIsEmpty()&&it== NetworkState.ERROR) View.VISIBLE else View.GONE

            if(!viewModel.listIsEmpty()){
                trendAdapter.setNetworkState(it)
            }
        })

    }
    private fun getViewModel(): TrendingViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TrendingViewModel(trendRepository) as T
            }
        })[TrendingViewModel::class.java]
    }

    @Subscribe(sticky = false,threadMode = ThreadMode.MAIN)
    fun navigateTrendingDetail(trendDetailMessage: TrendDetailMessage){
        val nextDetail=TrendingFragmentDirections.nextDetail(trendDetailMessage.trend)
        navController.navigate(nextDetail)
        Log.e("TrendingPageFragment","DetailFragment Navigation")

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
