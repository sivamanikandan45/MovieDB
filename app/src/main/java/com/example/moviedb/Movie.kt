package com.example.moviedb

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Movie(val id:Int,var title:String, var posterimg: Uri, var overview:String, var popularity:Double,var backgroundImg:Uri)