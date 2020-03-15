package com.yunuscagliyan.sinemalog.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.model.MovieDetail
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.source.MovieDetailDataSource
import io.reactivex.disposables.CompositeDisposable

class MovieDetailRepository(
    private val apiService:TheMovieDBInterface
) {
    lateinit var movieDetailDataSource:MovieDetailDataSource

    fun fetchSingleMovieDetails(
        compositeDisposable: CompositeDisposable,
        movieId:Int
    ):LiveData<MovieDetail>{
        movieDetailDataSource= MovieDetailDataSource(apiService,compositeDisposable)
        movieDetailDataSource.fetchMovieDetails(movieId)

        return movieDetailDataSource.downloadedMovieDetailResponse

    }
    fun getMovieDetailsNetworkState():LiveData<NetworkState>{
        return movieDetailDataSource.networkState
    }
}