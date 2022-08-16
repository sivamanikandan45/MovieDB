package com.example.moviedb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ReviewFragment : Fragment() {
    private lateinit var manager:LinearLayoutManager
    private lateinit var adapter: RatedListAdapter
    private lateinit var ratedMovieList:MutableList<RatedMovie>
    private lateinit var recyclerView:RecyclerView
    private val model:ListViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onResume() {
        super.onResume()
        println("fragmnet - Resume")
    }

    override fun onPause() {
        super.onPause()
        println("Pausing - Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ratedMovieDB=RatedMovieDB.getDB(context)

        val addReviewBtn:FloatingActionButton=view.findViewById(R.id.add_review)

        addReviewBtn.setOnClickListener{
            val dialog=MyRatingDialog()
            dialog.show(parentFragmentManager,"")

            /*GlobalScope.launch {
                val job=launch {
                    withContext(Dispatchers.IO){
                        ratedMovieList=ratedMovieDB.ratedMovieDao().getRatedList()
                    }
                    withContext(Dispatchers.Main){
                        adapter=RatedListAdapter(ratedMovieList,model)
                        recyclerView.adapter=adapter
                        recyclerView.adapter?.notifyItemInserted(0)
                    }
                }
                job.join()
            }*/
        }


        if(ratedMovieDB==null){
            println("No reviews found")
            val group=view.findViewById<Group>(R.id.group)
            group.visibility=View.VISIBLE
        }else{
            GlobalScope.launch {
                //ratedMovieList = mutableListOf<RatedMovie>()
                val job=launch {
                    withContext(Dispatchers.IO){
                        ratedMovieList=ratedMovieDB.ratedMovieDao().getRatedList()
                        //ratedMovieList= model.getRatedList()
                    }
                }
                job.join()
                withContext(Dispatchers.Main){
                    if(ratedMovieList.size==0){
                        println("No reviews found")
                        val group=view.findViewById<Group>(R.id.group)
                        group.visibility=View.VISIBLE
                    }
                    manager=LinearLayoutManager(context)
                    adapter=RatedListAdapter(ratedMovieList,model)
                    adapter.setOnItemClickListener(object:RatedListAdapter.ItemClickListener{
                        override fun onItemClick(position: Int) {
                            val dialog=RatingDescriptionDialog()
                            val ratedMovieViewModel:RatedMovieViewModel by activityViewModels()

                            val movieName:String=getNameById(ratedMovieList[position].id)
                            ratedMovieViewModel.movieName=movieName
                            ratedMovieViewModel.comment=ratedMovieList[position].comment
                            ratedMovieViewModel.rating=ratedMovieList[position].rating
                            //model.movieList[].title
                            dialog.show(parentFragmentManager,"")
                        }
                    })
                    recyclerView=view.findViewById<RecyclerView>(R.id.rated_movie_recycler)
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

    private fun getNameById(id: Int): String {

        for(movie in model.movieList){
            if(movie.id==id)
                return movie.title
        }
        return ""

    }


}