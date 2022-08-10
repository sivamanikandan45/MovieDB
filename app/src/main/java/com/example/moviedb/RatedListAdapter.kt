package com.example.moviedb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RatedListAdapter(private val list: List<RatedMovie>, val model: ListViewModel):RecyclerView.Adapter<RatedListAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var imageView:ImageView
        var rateMovieTitle:TextView
        var ratedValue:TextView
        var review:TextView
        init {
            imageView= view.findViewById(R.id.rated_movie_pic)
            rateMovieTitle=view.findViewById(R.id.rated_movie_title)
            ratedValue=view.findViewById(R.id.rated_value)
            review=view.findViewById(R.id.rated_movie_comment)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatedListAdapter.ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.rated_movie_item,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RatedListAdapter.ViewHolder, position: Int) {
        for(movie in model.movieList){
            if(movie.id==list[position].id){
                Picasso.get().load(movie.posterimg).into(holder.imageView)
                holder.rateMovieTitle.text=movie.title
            }
        }
        holder.ratedValue.text=list[position].rating.toString()+"/10"
        holder.review.text=list[position].comment
    }

    override fun getItemCount(): Int {
        return list.size
    }
}