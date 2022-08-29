package com.example.moviedb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MovieListAdapter():RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    private lateinit var listener:ItemClickListener
    private var viewType: ViewType =ViewType.GRID
    private lateinit var list: List<Movie>

    interface ItemClickListener{
        fun onItemClick(position:Int)
    }

    fun setOnItemClickListener(listener: ItemClickListener){
        this.listener=listener
    }

    fun setViewType(viewType: ViewType){
        this.viewType=viewType
        println("View type is ${this.viewType}")
    }

    override fun getItemViewType(position: Int): Int {
        return this.viewType.ordinal
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
        return if(viewType == ViewType.LIST.ordinal){
            val view=LayoutInflater.from(parent.context).inflate(R.layout.movie_list_listitem,parent,false)
            //println("creting list view")
            ViewHolder(view,listener)
        } else{
            val view=LayoutInflater.from(parent.context).inflate(R.layout.movie_list_item,parent,false)
            ViewHolder(view,listener)
        }
        /*val view=LayoutInflater.from(parent.context).inflate(R.layout.movie_list_item,parent,false)
        return ViewHolder(view,listener)*/
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.imageView.setImageURI(list[position].img)
        Picasso.get().load(list[position].posterimg).into(holder.imageView);
        holder.nameTextView.text=list[position].title
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(arrayList: ArrayList<Movie>) {
        this.list=arrayList
    }
}

