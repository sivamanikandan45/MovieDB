package com.example.moviedb

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MovieListAdapter(var list:List<Movie>):RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    private lateinit var listener:ItemClickListener

    interface ItemClickListener{
        public fun onItemClick(position:Int)
    }

    fun setOnItemClickListener(listener: ItemClickListener){
        this.listener=listener
    }

    class ViewHolder(view: View,listener: ItemClickListener):RecyclerView.ViewHolder(view){
        val nameTextView:TextView
        val imageView:ImageView
        init{
            nameTextView=view.findViewById(R.id.movie_title_card)
            imageView=view.findViewById(R.id.movie_image_card)

            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.movie_list_item,parent,false)
        return ViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.imageView.setImageURI(list[position].img)
        Picasso.get().load(list[position].img).into(holder.imageView);
        holder.nameTextView.text=list[position].title
    }

    override fun getItemCount(): Int {
        return list.size
    }
}