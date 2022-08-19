package com.example.moviedb

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
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

class HomeFragment : Fragment() {
    private lateinit var manager: GridLayoutManager
    private lateinit var recyclerView:RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lateinit var adapter:MovieListAdapter
        super.onViewCreated(view, savedInstanceState)
        manager= GridLayoutManager(activity,3)
        val movieListViewModel:ListViewModel by activityViewModels()
        GlobalScope.launch {
            val job= GlobalScope.launch {
                loadData(movieListViewModel)
            }
            job.join()
            adapter=MovieListAdapter(movieListViewModel.movieList)
            adapter.setOnItemClickListener(object :MovieListAdapter.ItemClickListener{
                override fun onItemClick(position: Int) {
                    //movieListViewModel.position=position
                    val intent= Intent(activity,MovieActivity::class.java)
                    val id=movieListViewModel.movieList[position].id

                    intent.putExtra("id",id)
                    //movieListViewModel.movie.value=movieListViewModel.movieList[position]
                    //println(movieListViewModel.movieList)
                    //intent.putExtra("movie",movieListViewModel.movie.value)
                    startActivity(intent)
                }
            })
            //val jsonObject = JSONTokener(con).nextValue() as JSONObject
            GlobalScope.launch(Dispatchers.Main) {
               // movieListViewModel.viewType.observe(this,)
                recyclerView=view.findViewById<RecyclerView>(R.id.recycler)
                recyclerView.adapter=adapter
                //manager.orientation=RecyclerView.HORIZONTAL
                recyclerView.layoutManager=manager
            }
        }

        movieListViewModel.viewType.observe(viewLifecycleOwner, Observer {
            if(it==ViewType.LIST){
                manager=GridLayoutManager(context,1)
                recyclerView=view.findViewById<RecyclerView>(R.id.recycler)
                recyclerView.layoutManager=manager
            }
            else{
                recyclerView=view.findViewById<RecyclerView>(R.id.recycler)
                manager=GridLayoutManager(context,3)
                recyclerView.layoutManager=manager
            }
        })
    }


    private suspend fun loadData(movieListViewModel: ListViewModel) {
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
                    val movie=Movie(id,title,(link+imgURI.toString()).toUri(),overview,popularity.toDouble(),(link+bgURI.toString()).toUri())
                    //movieListViewModel.movieList.add(movie)
                    //dbInstance.getDao().addMovie(movie)
                    list.add(movie)
                }
                movieListViewModel.movieList=list
            }
        }
    }
}