package com.example.moviedb

import androidx.lifecycle.ViewModel

class RatedMovieViewModel:ViewModel() {
    var imgUrl=""
    var movieName:String=""
    var rating:Int=0
    var comment:String=""
    var posterUrl=""
}