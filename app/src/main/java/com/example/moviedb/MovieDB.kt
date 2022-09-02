package com.example.moviedb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RatedMovie::class,Movie::class], version = 1)
abstract class MovieDB:RoomDatabase() {
    abstract fun ratedMovieDao():RatedMovieDao
    abstract fun movieDao():MovieDao

    companion object{
        @Volatile
        private var instance:MovieDB?=null

        fun getDB(context: Context?):MovieDB{
            val temp= instance
            if(temp != null){
                //dbInstance=Room.databaseBuilder(context, UserDB::class.java, "database-name").build()
                return temp
            }
            synchronized(this){
                val newInstance= Room.databaseBuilder(context!!, MovieDB::class.java, "rated_movie").build()
                instance=newInstance
                return newInstance
            }
            //return dbInstance
        }

    }
}