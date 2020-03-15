package com.yunuscagliyan.sinemalog.data.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.model.SingleMovie
import com.yunuscagliyan.sinemalog.data.source.MovieDataSource
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory (
    private val apiService:TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable
): DataSource.Factory<Int,SingleMovie>(){
    val movieDataSourceLiveData=MutableLiveData<MovieDataSource>()
    lateinit var movieDataSource: MovieDataSource
    override fun create(): DataSource<Int, SingleMovie> {
        movieDataSource= MovieDataSource(apiService,compositeDisposable)
        movieDataSourceLiveData.postValue(movieDataSource)
        return movieDataSource

    }

}