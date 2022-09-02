package com.example.moviedb

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(@PrimaryKey val id:Int, var title:String, var posterimg: String, var overview:String, var popularity:Double, var backgroundImg:String)