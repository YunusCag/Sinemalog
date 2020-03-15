package com.yunuscagliyan.sinemalog.data.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yunuscagliyan.sinemalog.data.api.TheMovieDBInterface
import com.yunuscagliyan.sinemalog.data.model.NetworkState
import com.yunuscagliyan.sinemalog.data.model.SeriesDetail
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.*

class SeriesDetailDataSource(
    private val apiService: TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) {

    private val _networkState=MutableLiveData<NetworkState>()
    val networkState:LiveData<NetworkState>
      get() =_networkState
    private val _downloadedSeriesDetailResponse=MutableLiveData<SeriesDetail>()
    val downloadedSeriesDetailResponse:LiveData<SeriesDetail>
      get() = _downloadedSeriesDetailResponse

    private fun getLanguage():String{
        val lang= Locale.getDefault().language
        return if(lang=="en"){
            lang
        }else{
            "tr"
        }
    }
    fun fetchSeriesDetails(seriesId:Int){
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.getSeriesDetails(seriesId,getLanguage())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedSeriesDetailResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailDataSource",it.message)

                        }
                    )

            )
        }catch (exception: Exception){

        }
    }
}