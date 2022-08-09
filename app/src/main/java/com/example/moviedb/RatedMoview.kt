package com.example.moviedb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RatedMovie(@PrimaryKey var id:Int, var rating:Int, var comment:String)