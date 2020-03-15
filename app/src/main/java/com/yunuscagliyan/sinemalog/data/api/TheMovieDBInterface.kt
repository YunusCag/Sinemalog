package com.yunuscagliyan.sinemalog.data.api

import com.yunuscagliyan.sinemalog.data.model.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TheMovieDBInterface {
    //https://api.themoviedb.org/3/trending/all/day?api_key=4ceba0985010b11eb871640206d56895
    // https://api.themoviedb.org/3/movie/popular?api_key=4ceba0985010b11eb871640206d56895&page=1
    // https://api.themoviedb.org/3/movie/299534?api_key=4ceba0985010b11eb871640206d56895
    // https://image.tmdb.org/t/p/w342/or06FN3Dka5tukK1e9sl16pB3iy.jpg
    @GET("trending/all/day?")
    fun getTrending(@Query("page")page:Int,@Query("language") lang:String):Single<Trending>
    @GET("movie/upcoming?")
    fun getPopularMovie(@Query("page")page: Int,@Query("language")lang: String):Single<MovieResponse>
    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id")id:Int,@Query("language") lang:String):Single<MovieDetail>
    @GET("tv/top_rated?")
    fun getTVSeries(@Query("page")page: Int,@Query("language")lang: String):Single<TVSeries>
    @GET("tv/{tv_id}")
    fun getSeriesDetails(@Path("tv_id")id:Int,@Query("language") lang:String):Single<SeriesDetail>


}