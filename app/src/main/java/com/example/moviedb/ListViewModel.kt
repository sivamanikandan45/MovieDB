package com.example.moviedb

import android.os.Parcel
import android.os.Parcelable
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListViewModel() :ViewModel(){

    val _movieList= mutableListOf<Movie>()
    var movieList=_movieList
    var movie:MutableLiveData<Movie> = MutableLiveData(Movie(1,"Example","https://www.google.com".toUri(),"Example",12.00,"https://www.google.com".toUri()))

    var viewType:MutableLiveData<ViewType> = MutableLiveData()

    var watchLaterList = mutableListOf<Movie>()

}