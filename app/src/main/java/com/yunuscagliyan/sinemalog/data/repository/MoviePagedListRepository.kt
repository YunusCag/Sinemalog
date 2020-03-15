package com.yunuscagliyan.sinemalog.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.yunuscagliyan.sinemalog.data.api.POST_PER_PAGE
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.factory.MovieDataSourceFactory
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.model.SingleMovie
import com.yunuscagliyan.sinemalog.data.source.MovieDataSource
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository(
    private val apiService:TheMovieDBInterface
) {
    lateinit var moviePagedList:LiveData<PagedList<SingleMovie>>
    lateinit var movieDataSourceFactory:MovieDataSourceFactory

    fun fetchLiveMoviePageList(
        compositeDisposable: CompositeDisposable
    ):LiveData<PagedList<SingleMovie>>{
        movieDataSourceFactory= MovieDataSourceFactory(apiService,compositeDisposable)
        val config=PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList=LivePagedListBuilder(movieDataSourceFactory,config).build()

        return moviePagedList
    }
    fun getNetworkState():LiveData<NetworkState>{
        return Transformations.switchMap<MovieDataSource,NetworkState>(
            movieDataSourceFactory.movieDataSourceLiveData,MovieDataSource::networkState
        )
    }
}