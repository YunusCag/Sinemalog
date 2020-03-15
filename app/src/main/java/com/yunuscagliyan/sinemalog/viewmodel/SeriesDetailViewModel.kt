package com.yunuscagliyan.sinemalog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.model.SeriesDetail
import com.yunuscagliyan.sinemalog.data.repository.SeriesDetailRepository
import io.reactivex.disposables.CompositeDisposable

class SeriesDetailViewModel(
    private val seriesRepository:SeriesDetailRepository,
    seriesId:Int
):ViewModel() {
    private val compositeDisposable=CompositeDisposable()

    val seriesDetail:LiveData<SeriesDetail> by lazy {
        seriesRepository.fetchSeriesDetail(compositeDisposable,seriesId)
    }
    val networkState:LiveData<NetworkState> by lazy {
        seriesRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}