package com.yunuscagliyan.sinemalog.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.yunuscagliyan.sinemalog.data.api.FIRST_PAGE
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.model.Trend
import com.yunuscagliyan.sinemalog.data.model.Trending
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class TrendingDataSource(
    private val apiService:TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable
):PageKeyedDataSource<Int, Trend>() {


    private val page= FIRST_PAGE
    val networkState=MutableLiveData<NetworkState>()

    private fun getLanguage():String{
        var lang=Locale.getDefault().language
        if(lang=="en"){
            return lang
        }else{
            return "tr"
        }
    }
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Trend>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getTrending(page,getLanguage())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.trends ,null,page+1)
                        networkState.postValue(NetworkState.LOADED)

                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource",it.message)

                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Trend>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getTrending(params.key,getLanguage())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(it.totalPages>=params.key){
                            callback.onResult(it.trends,params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        }else{
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        loadAfter(params,callback)
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("TrendingDataSource",it.message)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Trend>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}