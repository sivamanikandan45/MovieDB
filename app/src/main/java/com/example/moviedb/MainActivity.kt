package com.example.moviedb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class MainActivity : AppCompatActivity() {
    private lateinit var adapter:MovieListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val manager=GridLayoutManager(this,3)
        val movieListViewModel=ListViewModel()
        GlobalScope.launch {
            val job=GlobalScope.launch {
                loadData(movieListViewModel)
            }
            job.join()
            adapter=MovieListAdapter(movieListViewModel.movieList)
            adapter.setOnItemClickListener(object :MovieListAdapter.ItemClickListener{
                override fun onItemClick(position: Int) {
                    val intent=Intent(this@MainActivity,MovieActivity::class.java)
                    startActivity(intent)
                }
            })
            //val jsonObject = JSONTokener(con).nextValue() as JSONObject
            GlobalScope.launch(Dispatchers.Main) {
                val recyclerView=findViewById<RecyclerView>(R.id.recycler)
                recyclerView.adapter=adapter
                //manager.orientation=RecyclerView.HORIZONTAL
                recyclerView.layoutManager=manager

            }
        }

    }

    private suspend fun loadData(movieListViewModel: ListViewModel) {
        withContext(Dispatchers.IO){
            //val url="https://api.themoviedb.org/3/movie/top_rated?api_key=08e4a6a03c5c292c1893f7127324e5f3"
            val url="https://api.themoviedb.org/3/trending/movie/day?api_key=08e4a6a03c5c292c1893f7127324e5f3"
            val connection= URL(url).openConnection() as HttpURLConnection
            val reader=BufferedReader(InputStreamReader(connection.inputStream))
            
            var response=""
            var line=reader.readLine()
            while(line!=null){
                response+=line
                line=reader.readLine()
            }
            if(response.isNotEmpty()){
                val jsonObject=JSONTokener(response).nextValue() as JSONObject
                val jsonArray=jsonObject.getJSONArray("results")
                var list:MutableList<Movie> = mutableListOf()
                for(i in 0 until jsonArray.length()){
                    val imgURI=jsonArray.getJSONObject(i).getString("backdrop_path")
                    val overview=jsonArray.getJSONObject(i).getString("overview")
                    val title=jsonArray.getJSONObject(i).getString("original_title")
                    val popularity=jsonArray.getJSONObject(i).getString("popularity")
                    val movie=Movie(title,("https://image.tmdb.org/t/p/w500/"+imgURI.toString()).toUri(),overview,popularity.toDouble())
                    list.add(movie)
                }
                movieListViewModel.movieList=list
            }
        }
    }
}