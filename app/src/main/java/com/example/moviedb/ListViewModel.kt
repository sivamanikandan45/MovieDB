package com.example.moviedb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListViewModel:ViewModel() {
    private val _movieList= mutableListOf<Movie>()
    var movieList=_movieList
}