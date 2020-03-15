package com.yunuscagliyan.sinemalog.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.yunuscagliyan.sinemalog.data.api.POST_PER_PAGE
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.factory.TVSeriesDataSourceFactory
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.model.Series
import com.yunuscagliyan.sinemalog.data.source.TVSeriesDataSource
import io.reactivex.disposables.CompositeDisposable

class TVSeriesPagedListRepository(
    private val apiService:TheMovieDBInterface
) {
    lateinit var seriesPagedList:LiveData<PagedList<Series>>
    lateinit var tvSeriesDataSourceFactory:TVSeriesDataSourceFactory

    fun fetchLiveSeriesPageList(
        compositeDisposable: CompositeDisposable
    ):LiveData<PagedList<Series>>{
        tvSeriesDataSourceFactory= TVSeriesDataSourceFactory(apiService,compositeDisposable)
        val config=PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        seriesPagedList=LivePagedListBuilder(tvSeriesDataSourceFactory,config).build()

        return seriesPagedList
    }

    fun getNetworkState():LiveData<NetworkState>{
        return Transformations.switchMap<TVSeriesDataSource,NetworkState>(
            tvSeriesDataSourceFactory.tvSeriesLiveDataSource,TVSeriesDataSource::networkState
        )
    }
}
