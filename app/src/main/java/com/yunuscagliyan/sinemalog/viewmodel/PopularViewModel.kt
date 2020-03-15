package com.yunuscagliyan.sinemalog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.model.SingleMovie
import com.yunuscagliyan.sinemalog.data.repository.MoviePagedListRepository
import io.reactivex.disposables.CompositeDisposable

class PopularViewModel(
    private val moviePagedListRepository: MoviePagedListRepository
):ViewModel() {
    private val compositeDisposable=CompositeDisposable()

    val moviePagedList:LiveData<PagedList<SingleMovie>> by lazy {
        moviePagedListRepository.fetchLiveMoviePageList(compositeDisposable)
    }
    val neworkState:LiveData<NetworkState> by lazy {
        moviePagedListRepository.getNetworkState()
    }
    fun listIsEmpty():Boolean{
        return moviePagedList.value?.isEmpty()?:true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}