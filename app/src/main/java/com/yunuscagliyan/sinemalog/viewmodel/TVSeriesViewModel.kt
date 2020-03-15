package com.yunuscagliyan.sinemalog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.model.Series
import com.yunuscagliyan.sinemalog.data.repository.TVSeriesPagedListRepository
import io.reactivex.disposables.CompositeDisposable

class TVSeriesViewModel(
    private val repository: TVSeriesPagedListRepository
):ViewModel(){
    private val compositeDisposable=CompositeDisposable()
     val seriesPagedList: LiveData<PagedList<Series>> by lazy {
        repository.fetchLiveSeriesPageList(compositeDisposable)
    }
    val networkState:LiveData<NetworkState> by lazy {
        repository.getNetworkState()
    }
    fun listIsEmpty():Boolean{
        return seriesPagedList.value?.isEmpty()?:true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}