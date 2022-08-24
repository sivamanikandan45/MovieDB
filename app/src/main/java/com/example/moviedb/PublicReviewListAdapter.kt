package com.example.moviedb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PublicReviewListAdapter(var list:List<PublicReview>):RecyclerView.Adapter<PublicReviewListAdapter.ViewHolder>(){

    /*lateinit var list:List<PublicReview>

    fun setData(list:List<PublicReview>){
        this.list=list
    }*/

    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        var avatarImgView:ImageView
        var authorNameTv:TextView
        var accountTv:TextView
        var commentTv:TextView
        var ratingTv:TextView
        init{
            avatarImgView=view.findViewById(R.id.avatar)
            authorNameTv=view.findViewById(R.id.author_name)
            accountTv=view.findViewById(R.id.user_name)
            commentTv=view.findViewById(R.id.user_comment)
            ratingTv=view.findViewById(R.id.user_rated_value)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.user_review_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        if(list[position].avatar.toString()=="null")
            holder.avatarImgView.setImageResource(R.drawable.account)
        else if(list[position].avatar.toString().startsWith("/https://")){
            val substring=list[position].avatar.toString().substring(1,list[position].avatar.toString().lastIndex)
            Picasso.get().load(substring).into(holder.avatarImgView)
        }
        else
            Picasso.get().load("https://image.tmdb.org/t/p/w500"+list[position].avatar.toString()).into(holder.avatarImgView)
        holder.authorNameTv.text=list[position].name
        holder.accountTv.text=list[position].account
        holder.commentTv.text=list[position].comment
        holder.ratingTv.text=list[position].rating+"/10"
    }

    override fun getItemCount(): Int {
        return list.size
    }

}


