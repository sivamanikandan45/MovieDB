package com.example.moviedb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ReviewFragment : Fragment() {
    private lateinit var manager:LinearLayoutManager
    private lateinit var adapter: RatedListAdapter
    private lateinit var ratedMovieList:List<RatedMovie>
    private lateinit var recyclerView:RecyclerView
    private val viewModel:ListViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ratedMovieDB=RatedMovieDB.getDB(context)
        val group=view.findViewById<Group>(R.id.group)

        val addReviewBtn:FloatingActionButton=view.findViewById(R.id.add_review)

        addReviewBtn.setOnClickListener{
            val dialog=MyRatingDialog()
            dialog.show(parentFragmentManager,"")
        }

        //viewModel.getRatedMovieListSize()==0
        //println("list value is ${viewModel.getRatedMovieListSize()}")
        if(viewModel.getAllRatedMovieObservers().value?.size==0){
            println("No reviews found")
            val group=view.findViewById<Group>(R.id.group)
            group.visibility=View.VISIBLE
        }

        recyclerView=view.findViewById(R.id.public_review_recycler)
        viewModel.getAllRatedMovieObservers().observe(viewLifecycleOwner, Observer {
            adapter.setData(ArrayList(it))
            //println(viewModel.getAllRatedMovieObservers().value?.size)
            //group.visibility=View.INVISIBLE
            adapter.notifyDataSetChanged()
        })
        manager=LinearLayoutManager(context)
        adapter= RatedListAdapter(viewModel)
        GlobalScope.launch{
            val job=launch {
                adapter.setData(ArrayList(viewModel.getRatedMovieList()))
            }
            job.join()

            withContext(Dispatchers.Main){
                recyclerView.adapter=adapter
                recyclerView.layoutManager=manager
            }
        }

        adapter.setOnItemClickListener(object:RatedListAdapter.ItemClickListener {
            override fun onItemClick(position: Int) {
                val dialog = RatingDescriptionFragment()
                val ratedMovieViewModel: RatedMovieViewModel by activityViewModels()
                GlobalScope.launch {
                    val job=launch {
                        ratedMovieList=viewModel.getRatedMovieList()
                        println(ratedMovieList)
                    }
                    job.join()
                    val movieName:String=getNameById(ratedMovieList[position].id)
                    ratedMovieViewModel.movieName=movieName
                    ratedMovieViewModel.comment=ratedMovieList[position].comment
                    ratedMovieViewModel.rating=ratedMovieList[position].rating
                    ratedMovieViewModel.imgUrl=getImageUrlById(ratedMovieList[position].id)
                    ratedMovieViewModel.posterUrl=getPosterUrlById(ratedMovieList[position].id)
                    //dialog.show(parentFragmentManager,"")
                    /*val container=view.findViewById<FragmentContainerView>(R.id.fragmentContainerView)
                    container*/
                    parentFragmentManager.commit {
                        setReorderingAllowed(true)
                        addToBackStack(null)
                        replace(R.id.fragmentContainerView,RatingDescriptionFragment())
                    }
                }
            }
        })
    }

    private fun getPosterUrlById(id: Int): String {
        for(movie in viewModel.movieList){
            if(movie.id==id){
                return movie.backgroundImg.toString()
            }
        }
        return ""
    }

    private fun getNameById(id: Int): String {
        for(movie in viewModel.movieList){
            if(movie.id==id)
                return movie.title
        }
        return ""
    }
    private fun getImageUrlById(id:Int):String{
        for(movie in viewModel.movieList){
            if(movie.id==id){
                return movie.posterimg.toString()
            }
        }
        return ""
    }


}