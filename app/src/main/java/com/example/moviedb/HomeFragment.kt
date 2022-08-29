package com.example.moviedb

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
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

class HomeFragment : Fragment() {
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter:MovieListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    /*override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }*/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_fragment_menu,menu)
        val searchView=menu?.findItem(R.id.my_review_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchData(newText)
                return true
            }
        })
    }

    private fun searchData(newText: String?) {
        var list= mutableListOf<Movie>()
        val movieListViewModel:ListViewModel by activityViewModels()
        for(movie in movieListViewModel.movieList){
            if(movie.title.lowercase().contains(newText!!.lowercase())){
                list.add(movie)
            }
        }
        adapter.setData(ArrayList(list))
        adapter.notifyDataSetChanged()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //adapter:MovieListAdapter
        super.onViewCreated(view, savedInstanceState)
        /*(activity as AppCompatActivity).supportActionBar?.hide()*/
        (activity as AppCompatActivity).supportActionBar?.title="MovieDB"
        (activity as AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        manager= GridLayoutManager(activity,3)
        val movieListViewModel:ListViewModel by activityViewModels()
        GlobalScope.launch {
            val job= GlobalScope.launch {
                loadData(movieListViewModel)
            }
            job.join()
            adapter=MovieListAdapter()
            adapter.setData(ArrayList(movieListViewModel.movieList))
            if(movieListViewModel.viewType.value==ViewType.GRID){
                adapter.setViewType(ViewType.GRID)
            }else if(movieListViewModel.viewType.value==ViewType.LIST){
                adapter.setViewType(ViewType.LIST)
            }
            adapter.setOnItemClickListener(object :MovieListAdapter.ItemClickListener{
                override fun onItemClick(position: Int) {
                    val intent= Intent(activity,MovieActivity::class.java)
                    val id=movieListViewModel.movieList[position].id
                    intent.putExtra("id",id)
                    startActivity(intent)
                }
            })
            GlobalScope.launch(Dispatchers.Main) {
                recyclerView=view.findViewById<RecyclerView>(R.id.recycler)
                recyclerView.adapter=adapter
                recyclerView.layoutManager=manager
            }
        }

        movieListViewModel.viewType.observe(viewLifecycleOwner, Observer {
            if(it==ViewType.LIST){
                manager=LinearLayoutManager(context)
                recyclerView=view.findViewById<RecyclerView>(R.id.recycler)
                adapter= MovieListAdapter()
                adapter.setData(ArrayList(movieListViewModel.movieList))
                adapter.setViewType(ViewType.LIST)
                adapter.setOnItemClickListener(object :MovieListAdapter.ItemClickListener{
                    override fun onItemClick(position: Int) {
                        val intent= Intent(activity,MovieActivity::class.java)
                        val id=movieListViewModel.movieList[position].id
                        intent.putExtra("id",id)
                        startActivity(intent)
                    }
                })
                recyclerView.layoutManager=manager
            }
            else{
                recyclerView=view.findViewById<RecyclerView>(R.id.recycler)
                manager=GridLayoutManager(context,3)
                adapter=MovieListAdapter()
                adapter.setData(ArrayList(movieListViewModel.movieList))
                adapter.setViewType(ViewType.GRID)
                adapter.setOnItemClickListener(object :MovieListAdapter.ItemClickListener{
                    override fun onItemClick(position: Int) {
                        val intent= Intent(activity,MovieActivity::class.java)
                        val id=movieListViewModel.movieList[position].id
                        intent.putExtra("id",id)
                        startActivity(intent)
                    }
                })
                recyclerView.adapter=adapter
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