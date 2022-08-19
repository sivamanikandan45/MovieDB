package com.example.moviedb

import android.app.Application
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ListViewModel(application: Application) :AndroidViewModel(application){

    val _movieList= mutableListOf<Movie>()
    var movieList=_movieList
    var movie:MutableLiveData<Movie> = MutableLiveData(Movie(1,"Example","https://www.google.com".toUri(),"Example",12.00,"https://www.google.com".toUri()))

    var viewType:MutableLiveData<ViewType> = MutableLiveData()

    lateinit var allRatedMovies:MutableLiveData<List<RatedMovie>>
    init {
        allRatedMovies= MutableLiveData()
    }

    fun getAllRatedMovieObservers():MutableLiveData<List<RatedMovie>>
    {
        return allRatedMovies
    }

    private fun getAllRatedMovies(){
        val dao=RatedMovieDB.getDB(getApplication<Application?>().applicationContext).ratedMovieDao()
        val list=dao.getRatedList()
        allRatedMovies.postValue(list)
    }

    fun insertRatedMovie(movie: RatedMovie){
        RatedMovieDB.getDB(getApplication<Application?>().applicationContext).ratedMovieDao().addReview(movie)
        getAllRatedMovies()
    }

    fun updateRatedMovie(movie:RatedMovie){
        RatedMovieDB.getDB(getApplication<Application?>().applicationContext).ratedMovieDao().updateReview(movie)
        getAllRatedMovies()
    }

    fun getId(movieName: String):Int {
        for (movie in movieList) {
            if (movie.title==movieName) {
                return movie.id
            }
        }
        return -1
    }

    suspend fun getRatedMovieList():List<RatedMovie>{
        val dao=RatedMovieDB.getDB(getApplication<Application?>().applicationContext).ratedMovieDao()
        return dao.getRatedList()
    }
}