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
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ActorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actor)
        val id=intent.getIntExtra("id",0)
        GlobalScope.launch {
            val job=launch(Dispatchers.IO) {
                val link="https://image.tmdb.org/t/p/w500/"
                val url="https://api.themoviedb.org/3/person/$id?api_key=08e4a6a03c5c292c1893f7127324e5f3&language=en-US"
                val connection=URL(url).openConnection() as HttpURLConnection

                val reader=BufferedReader(InputStreamReader(connection.inputStream))
                var response=""
                var line=reader.readLine()
                while(line!=null){
                    response+=line
                    line=reader.readLine()
                }
                if(response.isNotEmpty()){
                    val jsonObject=JSONTokener(response).nextValue() as JSONObject
                    val name=jsonObject.getString("name")
                    val biography=jsonObject.getString("biography")
                    val birthday=jsonObject.getString("birthday")
                    val pob=jsonObject.getString("place_of_birth")
                    val profile=jsonObject.getString("profile_path")
                    val popularity=jsonObject.getString("popularity")

                    withContext(Dispatchers.Main){
                        val castName=findViewById<TextView>(R.id.cast_name)
                        val bio=findViewById<TextView>(R.id.overview)
                        val birthdayTextView:TextView=findViewById(R.id.dob)
                        val pobTextView:TextView=findViewById(R.id.pob)
                        val popularityTextView:TextView=findViewById(R.id.popularity)
                        val artistImg:ImageView=findViewById(R.id.cast_img)
                        val notAvailable="N/A"
                        castName.text=name


                        if(biography=="null"||biography=="")
                            bio.text=notAvailable
                        else
                            bio.text=biography

                        if(birthday=="null")
                            birthdayTextView.text=notAvailable
                        else
                            birthdayTextView.text=birthday

                        if(pob=="null")
                            pobTextView.text=notAvailable
                        else
                            pobTextView.text=pob

                        popularityTextView.text=popularity

                        Picasso.get().load(link+profile).into(artistImg)
                    }
                }
            }
            job.join()

        }

        val backBtn=findViewById<ImageView>(R.id.back)
        backBtn.setOnClickListener{
            super.onBackPressed()
        }
    }
}