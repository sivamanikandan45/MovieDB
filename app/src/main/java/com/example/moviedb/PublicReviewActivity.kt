package com.example.moviedb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class PublicReviewActivity : AppCompatActivity() {
    private lateinit var manager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter:PublicReviewListAdapter
    private lateinit var list:List<PublicReview>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_public_review)

        val id=intent.getIntExtra("id",0)

        GlobalScope.launch {
            val job=launch{
                list=getPublicReviewFromApi(id)
            }
            job.join()
            withContext(Dispatchers.Main){
                recyclerView=findViewById(R.id.public_review_recycler)
                adapter= PublicReviewListAdapter(list)
                //adapter.setData(list)
                manager=LinearLayoutManager(this@PublicReviewActivity)
                recyclerView.adapter=adapter
                recyclerView.layoutManager=manager
            }
        }
    }

    private suspend fun getPublicReviewFromApi(id:Int): List<PublicReview> {
        var list:MutableList<PublicReview> = mutableListOf()
        withContext(Dispatchers.IO){
            val url="https://api.themoviedb.org/3/movie/$id/reviews?api_key=08e4a6a03c5c292c1893f7127324e5f3&language=en-US&page=1"
            val connection=URL(url).openConnection() as HttpURLConnection
            val reader=BufferedReader(InputStreamReader(connection.inputStream))
            var response=""
            var line=reader.readLine()
            while(line!=null){
                response+=line
                line=reader.readLine()
            }
            if(response.isNotEmpty()){
                val jsonObject=JSONTokener(response).nextValue() as JSONObject
                val jsonArray=jsonObject.getJSONArray("results")
                for(i in 0 until jsonArray.length()){
                    val reviewObject=jsonArray.getJSONObject(i).getJSONObject("author_details")
                    val imgUrl=reviewObject.getString("avatar_path")
                    var comment=jsonArray.getJSONObject(i).getString("content")
                    var userName=reviewObject.getString("name")
                    val account="@"+reviewObject.getString("username")
                    var rating=reviewObject.getString("rating")

                    if(userName=="")
                        userName="Unknown User"

                    if(rating=="null")
                        rating="NA"

                    val publicReview=PublicReview(imgUrl.toUri(),userName,account,comment,rating)
                    list.add(publicReview)
                }
            }
        }
        return list
    }
}