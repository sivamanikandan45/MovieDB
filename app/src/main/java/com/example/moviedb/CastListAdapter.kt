package com.example.moviedb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CastListAdapter(var list:MutableList<Cast>) :RecyclerView.Adapter<CastListAdapter.ViewHolder>(){
    private lateinit var listener: ItemClickListener

    fun setOnItemClickListener(listener:ItemClickListener){
        this.listener=listener
    }

    interface ItemClickListener{
        fun onItemClick(position: Int)
    }

    class ViewHolder(view:View,listener: ItemClickListener):RecyclerView.ViewHolder(view){
        var artistImage:ImageView
        var artistName:TextView
        var charName:TextView
        init {
            artistImage=view.findViewById(R.id.artist_image)
            artistName=view.findViewById(R.id.artist_name)
            charName=view.findViewById(R.id.char_name)

            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastListAdapter.ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.cast_item,parent,false)
        return ViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: CastListAdapter.ViewHolder, position: Int) {
        holder.artistName.text=list[position].name
        holder.charName.text=list[position].charName
        Picasso.get().load(list[position].artistImg).into(holder.artistImage);
    }

    override fun getItemCount(): Int {
        return list.size
    }

}