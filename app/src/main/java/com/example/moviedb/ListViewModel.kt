package com.example.moviedb

import android.app.Application
import android.os.Parcel
import android.os.Parcelable
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListViewModel(application: Application) :AndroidViewModel(application){
    /*fun getRatedList(): LiveData<MutableList<RatedMovie>> {
        return ratedMovieList
    }*/


    val _movieList= mutableListOf<Movie>()
    var movieList=_movieList
    var movie:MutableLiveData<Movie> = MutableLiveData(Movie(1,"Example","https://www.google.com".toUri(),"Example",12.00,"https://www.google.com".toUri()))

    var viewType:MutableLiveData<ViewType> = MutableLiveData()
    /*lateinit var ratedMovieList:LiveData<MutableList<RatedMovie>>
    init {
        GlobalScope.launch {
            val job=launch {
                ratedMovieList=RatedMovieDB.getDB(getApplication<Application?>().applicationContext)?.ratedMovieDao().getRatedLiveList()
            }
            job.join()
            //var ratedMovieList=RatedMovieDB.getDB(getApplication<Application?>().applicationContext)?.ratedMovieDao().getRatedLiveList()
        }
    }*/


    fun getId(movieName: String):Int {
        for (movie in movieList) {
            if (movie.title==movieName) {
                return movie.id
            }
        }
        return -1
    }

}