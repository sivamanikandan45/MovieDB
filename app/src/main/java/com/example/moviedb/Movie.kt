package com.example.moviedb

import android.net.Uri

data class Movie(var title:String, var img: Uri, var overview:String,var popularity:Double)