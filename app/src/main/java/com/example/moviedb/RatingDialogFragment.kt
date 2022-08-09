package com.example.moviedb

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RatingDialogFragment:DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dlalog_rating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val submit:Button=view.findViewById(R.id.submit)
        val cancel:Button=view.findViewById(R.id.cancel)

        cancel.setOnClickListener {
            dismiss()
        }

        submit.setOnClickListener {
            GlobalScope.launch {
                val job=launch {
                    addRating(view)
                }
                job.join()
            }
            dismiss()

        }
    }

    private suspend fun addRating(view: View) {
        withContext(Dispatchers.IO){
            val dbInstance=RatedMovieDB.getDB(context)

            val sharedPreferences=context?.getSharedPreferences("RatingId",Context.MODE_PRIVATE)
            val id=sharedPreferences?.getInt("id",0)

            val ratingBar:RatingBar=view.findViewById(R.id.ratingBar)
            val ratedValue=ratingBar.rating.toInt()
            val comment=view.findViewById<TextInputEditText>(R.id.comment).text

            val ratedMovie= id?.let { RatedMovie(it,ratedValue,comment.toString()) }
            if(ratedMovie!=null){
                dbInstance.ratedMovieDao().addReview(ratedMovie)
            }


        }
    }
}