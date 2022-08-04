package com.example.moviedb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class MovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        //val position=intent.getIntExtra("position",0)
        //println("got $position")
        val selectedMovie=intent.getParcelableExtra<Movie>("movie")
        println(selectedMovie)

        //val selectedMovie= model?.movieList?.get(position)
        val movieImg:ImageView=findViewById(R.id.movie_pic)
        val movieName:TextView=findViewById(R.id.movie_title)
        val popularity:TextView=findViewById(R.id.popularity)
        val overview:TextView=findViewById(R.id.overview)

        //movieImg.setImageURI(selectedMovie?.img)
        //Picasso.get(movieImg).load(selectedMovie?.img)
        Picasso.get().load(selectedMovie?.backgroundImg).into(movieImg)
        movieName.text=selectedMovie?.title
        popularity.text=selectedMovie?.popularity.toString()
        overview.text=selectedMovie?.overview

    }
}