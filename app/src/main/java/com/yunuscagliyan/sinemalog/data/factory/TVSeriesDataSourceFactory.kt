package com.yunuscagliyan.sinemalog.data.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.model.Series
import com.yunuscagliyan.sinemalog.data.source.TVSeriesDataSource
import com.yunuscagliyan.sinemalog.data.source.TrendingDataSource
import io.reactivex.disposables.CompositeDisposable

class TVSeriesDataSourceFactory(
    private val apiService:TheMovieDBInterface,
    private val  compositeDisposable: CompositeDisposable
): DataSource.Factory<Int,Series>() {
    val tvSeriesLiveDataSource=MutableLiveData<TVSeriesDataSource>()
    lateinit var tvSeriesDataSource: TVSeriesDataSource

    override fun create(): DataSource<Int, Series> {
        tvSeriesDataSource=TVSeriesDataSource(apiService,compositeDisposable)

        tvSeriesLiveDataSource.postValue(tvSeriesDataSource)

        return  tvSeriesDataSource
    }
}