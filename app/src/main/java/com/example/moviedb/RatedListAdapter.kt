package com.example.moviedb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RatedListAdapter(val model: ListViewModel):RecyclerView.Adapter<RatedListAdapter.ViewHolder>() {
    private lateinit var listener:ItemClickListener

    var list=ArrayList<RatedMovie>()

    fun setData(list:ArrayList<RatedMovie>){
        this.list=list
    }

    fun setOnItemClickListener(listener:ItemClickListener){
        this.listener=listener
    }

    interface ItemClickListener{
        fun onItemClick(position: Int)
    }

    class ViewHolder(view: View,listener:ItemClickListener):RecyclerView.ViewHolder(view){
        var imageView:ImageView
        var rateMovieTitle:TextView
        var ratedValue:TextView
        var review:TextView
        init {
            imageView= view.findViewById(R.id.rated_movie_pic)
            rateMovieTitle=view.findViewById(R.id.rated_movie_title)
            ratedValue=view.findViewById(R.id.user_rated_value)
            review=view.findViewById(R.id.user_comment)
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatedListAdapter.ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.rated_movie_item,parent,false)
        return  ViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: RatedListAdapter.ViewHolder, position: Int) {
        for(movie in model.movieList){
            if(movie.id== list[position].id){
                Picasso.get().load(movie.posterimg).into(holder.imageView)
                holder.rateMovieTitle.text=movie.title
            }
        }
        holder.ratedValue.text= list[position].rating.toString()+"/10"
        holder.review.text=list[position].comment
    }

    override fun getItemCount(): Int {
        return list.size
    }
}