package com.example.moviedb

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels

class RatingDescriptionDialog:DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_rating_description,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val okBtn:Button=view.findViewById(R.id.ok)
        //val preferences=context?.getSharedPreferences("review_preference",Context.MODE_PRIVATE)
        val ratedMovieViewModel:RatedMovieViewModel by activityViewModels()
        val rating:RatingBar=view.findViewById(R.id.ratingBar)
        val comment:TextView=view.findViewById(R.id.user_review)
        val movieName:TextView=view.findViewById(R.id.textView)

        movieName.text=ratedMovieViewModel.movieName
        rating.rating=ratedMovieViewModel.rating.toFloat()
        comment.text=ratedMovieViewModel.comment

        okBtn.setOnClickListener {
            dismiss()
        }

    }
}