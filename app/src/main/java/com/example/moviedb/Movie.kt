package com.example.moviedb

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Movie(var title:String, var posterimg: Uri, var overview:String, var popularity:Double,var backgroundImg:Uri):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readParcelable(Uri::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readParcelable(Uri::class.java.classLoader)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeParcelable(posterimg, flags)
        parcel.writeString(overview)
        parcel.writeDouble(popularity)
        parcel.writeParcelable(backgroundImg, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}