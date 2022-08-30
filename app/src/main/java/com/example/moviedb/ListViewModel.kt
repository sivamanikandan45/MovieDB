package com.example.moviedb

import android.app.Application
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ListViewModel(application: Application) :AndroidViewModel(application){

    val _movieList= mutableListOf<Movie>()
    var movieList=_movieList
    //var movieList:MutableLiveData<List<Movie>> = MutableLiveData<List<Movie>>()
    var movie:MutableLiveData<Movie> = MutableLiveData<Movie>()

    var viewType:MutableLiveData<ViewType> = MutableLiveData()

    lateinit var allRatedMovies:MutableLiveData<List<RatedMovie>>
    init {
        allRatedMovies= MutableLiveData()
        viewType.value=ViewType.GRID
        /*viewModelScope.launch {
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
                    val movie=Movie(id,title,(link+imgURI.toString()).toUri(),overview,popularity.toDouble(),(link+bgURI.toString()).toUri())
                    //movieListViewModel.movieList.add(movie)
                    //dbInstance.getDao().addMovie(movie)
                    list.add(movie)
                }
                movieList=list
        }*/
    }

    fun getAllRatedMovieObservers():MutableLiveData<List<RatedMovie>>
    {
        return allRatedMovies
    }

    private fun getAllRatedMovies(){
        val dao=RatedMovieDB.getDB(getApplication<Application?>().applicationContext).ratedMovieDao()
        val list=dao.getRatedList()
        allRatedMovies.postValue(list)
    }

    fun insertRatedMovie(movie: RatedMovie){
        RatedMovieDB.getDB(getApplication<Application?>().applicationContext).ratedMovieDao().addReview(movie)
        getAllRatedMovies()
    }

    fun updateRatedMovie(movie:RatedMovie){
        RatedMovieDB.getDB(getApplication<Application?>().applicationContext).ratedMovieDao().updateReview(movie)
        getAllRatedMovies()
    }

    fun getId(movieName: String):Int {
        for (movie in movieList) {
            if (movie.title==movieName) {
                return movie.id
            }
        }
        return -1
    }

    suspend fun getRatedMovieList():List<RatedMovie>{
        val dao=RatedMovieDB.getDB(getApplication<Application?>().applicationContext).ratedMovieDao()
        return dao.getRatedList()
    }

    fun getRatedMovieListSize(): Int? {
        return allRatedMovies.value?.size
    }
}