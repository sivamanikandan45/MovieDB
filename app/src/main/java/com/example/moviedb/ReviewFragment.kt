package com.example.moviedb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ReviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ratedMovieDB=RatedMovieDB.getDB(context)
        val model:ListViewModel by activityViewModels()
        if(ratedMovieDB==null){
            println("No reviews found")
            val group=view.findViewById<Group>(R.id.group)
            group.visibility=View.VISIBLE
        }else{
            GlobalScope.launch {
                var ratedMovieList = mutableListOf<RatedMovie>()
                val job=launch {
                    withContext(Dispatchers.IO){
                        ratedMovieList=ratedMovieDB.ratedMovieDao().getRatedList()
                    }
                }
                job.join()
                withContext(Dispatchers.Main){
                    if(ratedMovieList.size==0){
                        println("No reviews found")
                        val group=view.findViewById<Group>(R.id.group)
                        group.visibility=View.VISIBLE
                    }
                    val manager=LinearLayoutManager(context)
                    val adapter=RatedListAdapter(ratedMovieList,model)
                    val recyclerView=view.findViewById<RecyclerView>(R.id.rated_movie_recycler)
                    recyclerView.adapter=adapter
                    recyclerView.layoutManager=manager
                    /*val dividerItemDecoration = DividerItemDecoration(recyclerView.context, manager.getOrientation())
                    recyclerView.addItemDecoration(dividerItemDecoration)*/
                    val divider = MaterialDividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
                    recyclerView.addItemDecoration(divider)

                }
            }
        }
    }

}