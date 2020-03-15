package com.yunuscagliyan.sinemalog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yunuscagliyan.sinemalog.data.model.MovieDetail
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.repository.MovieDetailRepository
import io.reactivex.disposables.CompositeDisposable

class MovieDetailViewModel(
    private val movieDetailRepository: MovieDetailRepository,
    movieId:Int
):ViewModel() {
    private val compositeDisposable=CompositeDisposable()

    val movieDetail:LiveData<MovieDetail> by lazy {
        movieDetailRepository.fetchSingleMovieDetails(compositeDisposable,movieId)
    }
    val networkState:LiveData<NetworkState> by lazy {
        movieDetailRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}