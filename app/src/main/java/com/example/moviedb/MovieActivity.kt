package com.example.moviedb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        //val position=intent.getIntExtra("position",0)
        //println("got $position")
        //val selectedMovie=intent.getParcelableExtra<Movie>("id")
        val id=intent.getIntExtra("id",0)

        GlobalScope.launch {
            getData(id)
        }

        val rateThis:TextView=findViewById(R.id.rate_this)
        rateThis.setOnClickListener{
            println("Rating this")
            val dialog=RatingDialogFragment()
            val sharedPreferences=getSharedPreferences("RatingId", MODE_PRIVATE)
            val editor=sharedPreferences.edit()
            editor.putInt("id",id)
            editor.apply()
            dialog.show(supportFragmentManager,"")
        }

        val watchLater:TextView=findViewById(R.id.watch_later)
        watchLater.setOnClickListener{
            println("Added to watch later")
            //Toast.makeText(this,"Added to watchLater",Toast.LENGTH_SHORT).show()
        }

        /*println(selectedMovie)

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
        overview.text=selectedMovie?.overview*/

    }

    private suspend fun getData(id: Int) {
        withContext(Dispatchers.IO){
            val url= "https://api.themoviedb.org/3/movie/$id?api_key=08e4a6a03c5c292c1893f7127324e5f3"
            //val url="https://api.themoviedb.org/3/trending/movie/day?api_key=08e4a6a03c5c292c1893f7127324e5f3"
            val connection= URL(url).openConnection() as HttpURLConnection
            val reader= BufferedReader(InputStreamReader(connection.inputStream))

            var response=""
            var line=reader.readLine()
            while(line!=null){
                response+=line
                line=reader.readLine()
            }

            if(response.isNotEmpty()){
                val jsonObject = JSONTokener(response).nextValue() as JSONObject
                val backgroundImg=jsonObject.getString("backdrop_path")
                val title=jsonObject.getString("original_title")
                val tagline=jsonObject.getString("tagline")
                val overviewText=jsonObject.getString("overview")
                val rating=jsonObject.getString("vote_average")
                val rate_count=jsonObject.getString("vote_count")
                val popularityCount=jsonObject.getString("popularity")
                
                withContext(Dispatchers.Main){
                    val link="https://image.tmdb.org/t/p/w500/"
                    val movieImg:ImageView=findViewById(R.id.movie_pic)
                    val movieName:TextView=findViewById(R.id.movie_title)
                    val popularity:TextView=findViewById(R.id.popularity)
                    val overview:TextView=findViewById(R.id.overview)
                    val taglineTextView:TextView=findViewById(R.id.tagline)
                    val ratingValue:TextView=findViewById(R.id.rating)
                    val ratingCount:TextView=findViewById(R.id.rating_count)
                    
                    //movieImg.setImageURI(backgroundImg.toUri())
                    Picasso.get().load(link+backgroundImg).into(movieImg)
                    movieName.text=title
                    popularity.text=popularityCount
                    overview.text=overviewText
                    taglineTextView.text=tagline
                    ratingValue.text=rating+"/10"
                    ratingCount.text=rate_count
                }

            }
        }


    }
}