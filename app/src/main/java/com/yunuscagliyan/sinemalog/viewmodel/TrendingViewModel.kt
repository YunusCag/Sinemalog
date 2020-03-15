package com.yunuscagliyan.sinemalog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.model.Trend
import com.yunuscagliyan.sinemalog.data.repository.TrendingPagedListRepository
import io.reactivex.disposables.CompositeDisposable

class TrendingViewModel(
    private val trendingPagedListRepository: TrendingPagedListRepository
):ViewModel() {
    private val compositeDisposable=CompositeDisposable()
    val trendingPagedList:LiveData<PagedList<Trend>> by lazy{
        trendingPagedListRepository.fetchLiveTrendsPageList(compositeDisposable)
    }
    val networkState:LiveData<NetworkState> by lazy {
        trendingPagedListRepository.getNetworkState()
    }
    fun listIsEmpty():Boolean{
        return trendingPagedList.value?.isEmpty()?:true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}