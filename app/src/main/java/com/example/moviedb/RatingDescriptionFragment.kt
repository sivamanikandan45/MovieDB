package com.example.moviedb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.squareup.picasso.Picasso

class RatingDescriptionFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rating_description,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val okBtn:Button=view.findViewById(R.id.ok)
        //val preferences=context?.getSharedPreferences("review_preference",Context.MODE_PRIVATE)
        val ratedMovieViewModel:RatedMovieViewModel by  activityViewModels()
//        val rating:RatingBar=view.findViewById(R.id.ratingBar)
        val comment:TextView=view.findViewById(R.id.user_review)
        val movieName:TextView=view.findViewById(R.id.textView)
        val ratingValue:TextView=view.findViewById(R.id.rating)
        val imageView:ImageView=view.findViewById(R.id.shapeableImageView)
        val bgImgView:ImageView=view.findViewById(R.id.poster)

        movieName.text=ratedMovieViewModel.movieName
        //rating.rating=ratedMovieViewModel.rating.toFloat()
        comment.text=ratedMovieViewModel.comment
        ratingValue.text=ratedMovieViewModel.rating.toString()+"/10"
        Picasso.get().load(ratedMovieViewModel.imgUrl).into(imageView)
        Picasso.get().load(ratedMovieViewModel.posterUrl).into(bgImgView)


        val back=view.findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            activity?.onBackPressed()
        }

        /*okBtn.setOnClickListener {
            //dismiss()
        }*/

    }
}