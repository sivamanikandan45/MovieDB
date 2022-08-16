package com.example.moviedb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RatedMovie::class], version = 1)
abstract class RatedMovieDB:RoomDatabase() {
    abstract fun ratedMovieDao():RatedMovieDao

    companion object{
        @Volatile
        private var instance:RatedMovieDB?=null

        fun getDB(context: Context?):RatedMovieDB{
            val temp= instance
            if(temp != null){
                //dbInstance=Room.databaseBuilder(context, UserDB::class.java, "database-name").build()
                return temp
            }
            synchronized(this){
                val newInstance= Room.databaseBuilder(context!!, RatedMovieDB::class.java, "rated_movie").build()
                instance=newInstance
                return newInstance
            }
            //return dbInstance
        }

    }
}