package com.yunuscagliyan.sinemalog.data.repository

import androidx.lifecycle.LiveData
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.model.SeriesDetail
import com.yunuscagliyan.sinemalog.data.source.SeriesDetailDataSource
import io.reactivex.disposables.CompositeDisposable

class SeriesDetailRepository(
    private val apiService:TheMovieDBInterface
) {
    lateinit var seriesDetailDataSource:SeriesDetailDataSource

    fun fetchSeriesDetail(
        compositeDisposable: CompositeDisposable,
        seriesId:Int
    ):LiveData<SeriesDetail>{
        seriesDetailDataSource= SeriesDetailDataSource(apiService,compositeDisposable)
        seriesDetailDataSource.fetchSeriesDetails(seriesId)

        return seriesDetailDataSource.downloadedSeriesDetailResponse
    }
    fun getMovieDetailsNetworkState():LiveData<NetworkState>{
        return seriesDetailDataSource.networkState
    }
}