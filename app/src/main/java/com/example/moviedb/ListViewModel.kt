package com.example.moviedb

import android.app.Application
import android.content.Context

import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class ListViewModel(application: Application) :AndroidViewModel(application){

    val _movieList= mutableListOf<Movie>()
    //var movieList=_movieList
    lateinit var movieList:MutableLiveData<List<Movie>>
    var movie:MutableLiveData<Movie> = MutableLiveData<Movie>()

    var viewType:MutableLiveData<ViewType> = MutableLiveData()

    lateinit var allRatedMovies:MutableLiveData<List<RatedMovie>>
    init {
        allRatedMovies= MutableLiveData()
        movieList= MutableLiveData()
        viewType.value=ViewType.GRID

        viewModelScope.launch {
            val job=launch {
                loadData()
            }
            job.join()
            //println(movieList)
            println("list is assigned in viewmodel scope")
        }

    }


    private suspend fun loadData() {
        withContext(Dispatchers.IO){
            val url="https://api.themoviedb.org/3/trending/movie/day?api_key=08e4a6a03c5c292c1893f7127324e5f3"
            val connection= URL(url).openConnection() as HttpURLConnection
            val reader= BufferedReader(InputStreamReader(connection.inputStream))
            var response=""
            var line=reader.readLine()
            while(line!=null){
                response+=line
                line=reader.readLine()
            }

            if(response.isNotEmpty()){
                val jsonObject= JSONTokener(response).nextValue() as JSONObject
                val jsonArray=jsonObject.getJSONArray("results")
                var list:MutableList<Movie> = mutableListOf()
                for(i in 0 until jsonArray.length()){
                    val link="https://image.tmdb.org/t/p/w500/"
                    val id=jsonArray.getJSONObject(i).getString("id").toInt()
                    val imgURI=jsonArray.getJSONObject(i).getString("poster_path")
                    val bgURI=jsonArray.getJSONObject(i).getString("backdrop_path")
                    val overview=jsonArray.getJSONObject(i).getString("overview")
                    val title=jsonArray.getJSONObject(i).getString("original_title")
                    val popularity=jsonArray.getJSONObject(i).getString("popularity")
                    val movie=Movie(id,title,(link+imgURI.toString()),overview,popularity.toDouble(),(link+bgURI.toString()))
                    //movieListViewModel.movieList.add(movie)
                    //dbInstance.getDao().addMovie(movie)
                    list.add(movie)

                    //println(list)
                }
                //movieList.postValue(list)
                insertMovieList(list)
                //println(movieList)
            }
        }
    }

    fun getAllRatedMovieObservers():MutableLiveData<List<RatedMovie>>
    {
        return allRatedMovies
    }

    fun getAllMovieObservers():MutableLiveData<List<Movie>>
    {
        return movieList
    }

    fun getAllRatedMovies(){
        val dao=MovieDB.getDB(getApplication<Application?>().applicationContext).ratedMovieDao()
        val list=dao.getRatedList()
        allRatedMovies.postValue(list)
    }

    fun getALlMovies(){
        val dao=MovieDB.getDB(getApplication<Application?>().applicationContext).movieDao()
        val list=dao.getMovieList()
        movieList.postValue(list)
    }

    fun insertMovieList(list:List<Movie>){
        val dao=MovieDB.getDB(getApplication<Application?>().applicationContext).movieDao()
        dao.insertMovieList(list)
        getALlMovies()
    }



    fun insertRatedMovie(movie: RatedMovie){
        MovieDB.getDB(getApplication<Application?>().applicationContext).ratedMovieDao().addReview(movie)
        getAllRatedMovies()
    }

    fun updateRatedMovie(movie:RatedMovie){
        MovieDB.getDB(getApplication<Application?>().applicationContext).ratedMovieDao().updateReview(movie)
        getAllRatedMovies()
    }

    fun getId(movieName: String):Int {
        for (movie in movieList.value!!) {
            if (movie.title==movieName) {
                return movie.id
            }
        }
        return -1
    }

    fun getMovieList():List<Movie>{
        val dao=MovieDB.getDB(getApplication<Application?>().applicationContext).movieDao()
        return dao.getMovieList()
    }

    fun getRatedMovieList():List<RatedMovie>{
        val dao=MovieDB.getDB(getApplication<Application?>().applicationContext).ratedMovieDao()
        return dao.getRatedList()
    }

    fun getRatedMovieListSize(): Int? {
        return allRatedMovies.value?.size
    }
}