package com.yunuscagliyan.sinemalog.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.yunuscagliyan.sinemalog.data.api.POST_PER_PAGE
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.factory.TrendingDataSourceFactory
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.model.Trend
import com.yunuscagliyan.sinemalog.data.source.TrendingDataSource
import io.reactivex.disposables.CompositeDisposable

class TrendingPagedListRepository (
    private val apiService:TheMovieDBInterface
){
    lateinit var trendPagedList:LiveData<PagedList<Trend>>
    lateinit var trendingDataSourceFactory:TrendingDataSourceFactory

    fun fetchLiveTrendsPageList(
        compositeDisposable: CompositeDisposable
    ):LiveData<PagedList<Trend>>{
        trendingDataSourceFactory= TrendingDataSourceFactory(apiService,compositeDisposable)
        val config=PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        trendPagedList=LivePagedListBuilder(trendingDataSourceFactory,config).build()

        return trendPagedList
    }
    fun getNetworkState():LiveData<NetworkState>{
        return Transformations.switchMap<TrendingDataSource,NetworkState>(
            trendingDataSourceFactory.trendingLiveDataSource,TrendingDataSource::networkState
        )
    }
}