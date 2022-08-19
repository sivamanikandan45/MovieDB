package com.example.moviedb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RatedMovieDao{

    @Insert
    fun addReview(ratedMovieDao: RatedMovie)

    @Query("SELECT * FROM RatedMovie")
    fun getRatedList():MutableList<RatedMovie>

    @Update
    fun updateReview(ratedMovie: RatedMovie)

}