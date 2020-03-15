package com.yunuscagliyan.sinemalog.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.yunuscagliyan.sinemalog.data.api.FIRST_PAGE
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.model.SingleMovie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class MovieDataSource (
    private val apiService:TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable
):PageKeyedDataSource<Int,SingleMovie>(){

    private val page= FIRST_PAGE
    val  networkState=MutableLiveData<NetworkState>()
    private fun getLanguage():String{
        val lang= Locale.getDefault().language
        return if(lang=="en"){
            lang
        }else{
            "tr"
        }
    }
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, SingleMovie>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopularMovie(page,getLanguage())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.singleMovies,null,page+1)
                        networkState.postValue(NetworkState.LOADED)

                },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource",it.message)

                    }
                )
        )

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, SingleMovie>) {

        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopularMovie(params.key,getLanguage())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(it.totalPages>=params.key){
                            callback.onResult(it.singleMovies,params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        }else{
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }


                    },
                    {
                        loadAfter(params,callback)
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource",it.message)

                    }
                )
        )

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, SingleMovie>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}