package com.example.moviedb

import android.net.Uri
import androidx.room.PrimaryKey

data class Movie(@PrimaryKey val id:Int, var title:String, var posterimg: Uri, var overview:String, var popularity:Double, var backgroundImg:Uri)