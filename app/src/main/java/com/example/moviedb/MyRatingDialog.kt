package com.example.moviedb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyRatingDialog:DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_my_rating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel:ListViewModel by activityViewModels()
        val dropDownTextField=view.findViewById<AutoCompleteTextView>(R.id.movie_dropdown)
        val list= mutableListOf<String>()
        for(movie in viewModel.movieList){
            list.add(movie.title)
        }

        var adapter = ArrayAdapter(requireContext(), R.layout.list_item, list)
        dropDownTextField.setAdapter(adapter)


        val cancelBtn=view.findViewById<Button>(R.id.cancel)
        cancelBtn.setOnClickListener {
            dismiss()
        }

        val submitBtn=view.findViewById<Button>(R.id.submit)
        submitBtn.setOnClickListener {
            GlobalScope.launch {
                val job=launch {
                    addRating(view,viewModel)
                }
                job.join()
            }
            dismiss()
        }
    }

    private suspend fun addRating(view: View, viewModel: ListViewModel) {
        withContext(Dispatchers.IO) {
            val dbInstance = RatedMovieDB.getDB(context)

            val textField=view.findViewById<AutoCompleteTextView>(R.id.movie_dropdown)
            val movieName=textField.text.toString()
            val id=getId(viewModel, movieName)

            val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
            val ratedValue = ratingBar.rating.toInt()
            val comment = view.findViewById<TextInputEditText>(R.id.comment).text

            val ratedMovie = id?.let { RatedMovie(it, ratedValue, comment.toString()) }
            if (ratedMovie != null) {
                val ratedList = dbInstance.ratedMovieDao().getRatedList()
                if (search(id, ratedList)) {
                    dbInstance.ratedMovieDao().updateReview(ratedMovie)
                } else {
                    dbInstance.ratedMovieDao().addReview(ratedMovie)
                }
            }
        }
    }

    private fun getId(viewModel: ListViewModel, movieName: String):Int {
        for (movie in viewModel.movieList) {
            if (movie.title==movieName) {
                return movie.id
            }
        }
        return -1
    }

    private fun search(id: Int, ratedList: MutableList<RatedMovie>):Boolean {
        for(ratedMovie in ratedList){
            if(ratedMovie.id==id){
                return true
            }
        }
        return false
    }
}