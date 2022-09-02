package com.example.moviedb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieList(movieList:List<Movie>)

    @Query("SELECT * FROM Movie")
    fun getMovieList():List<Movie>
}