package com.yunuscagliyan.sinemalog.data.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.model.MovieDetail
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.*

class MovieDetailDataSource(
    private val apiService:TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) {
    private val _networkState=MutableLiveData<NetworkState>()
    val networkState:LiveData<NetworkState>
           get() = _networkState
    private val _downloadedMovieDetailsResponse=MutableLiveData<MovieDetail>()
    val downloadedMovieDetailResponse:LiveData<MovieDetail>
         get() = _downloadedMovieDetailsResponse

    private fun getLanguage():String{
        val lang= Locale.getDefault().language
        return if(lang=="en"){
            lang
        }else{
            "tr"
        }
    }
    fun fetchMovieDetails(movieId:Int){
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId,getLanguage())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailDataSource",it.message)

                        }
                    )

            )
        }catch (exception:Exception){

        }
    }
}