package com.yunuscagliyan.sinemalog.data.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.model.Trend
import com.yunuscagliyan.sinemalog.data.source.TrendingDataSource
import io.reactivex.disposables.CompositeDisposable

class TrendingDataSourceFactory(
    private val apiService:TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable
): DataSource.Factory<Int,Trend>() {
    val trendingLiveDataSource=MutableLiveData<TrendingDataSource>()
    lateinit var  trendingDataSource:TrendingDataSource


    override fun create(): DataSource<Int, Trend> {
        trendingDataSource=TrendingDataSource(apiService,compositeDisposable)

        trendingLiveDataSource.postValue(trendingDataSource)
        return trendingDataSource

    }

}