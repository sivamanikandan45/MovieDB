package com.example.moviedb

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.squareup.picasso.Picasso

class RatingDescriptionFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rating_description,container,false)

    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.isFocusableInTouchMode = true
        view?.requestFocus()

        view?.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action === KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Toast.makeText(activity, "Back Pressed", Toast.LENGTH_SHORT).show()
                        return true
                    }
                }
                return false
            }
        })
    }*/


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //(activity as AppCompatActivity)?.supportActionBar?.setHomeButtonEnabled(true)
        //val okBtn:Button=view.findViewById(R.id.ok)
        //val preferences=context?.getSharedPreferences("review_preference",Context.MODE_PRIVATE)
        val ratedMovieViewModel:RatedMovieViewModel by  activityViewModels()
//        val rating:RatingBar=view.findViewById(R.id.ratingBar)
        val comment:TextView=view.findViewById(R.id.user_review)
        val movieName:TextView=view.findViewById(R.id.textView)
        val ratingValue:TextView=view.findViewById(R.id.rating)
        val imageView:ImageView=view.findViewById(R.id.shapeableImageView)
        //val bgImgView:ImageView=view.findViewById(R.id.poster)

        movieName.text=ratedMovieViewModel.movieName
        //rating.rating=ratedMovieViewModel.rating.toFloat()
        comment.text=ratedMovieViewModel.comment
        ratingValue.text=ratedMovieViewModel.rating.toString()+"/10"
        Picasso.get().load(ratedMovieViewModel.imgUrl).into(imageView)
        //Picasso.get().load(ratedMovieViewModel.posterUrl).into(bgImgView)



        /*okBtn.setOnClickListener {
            //dismiss()
        }*/

    }



    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(activity?.supportFragmentManager?.backStackEntryCount==0){
            return super.onOptionsItemSelected(item)
        }
        activity?.supportFragmentManager?.popBackStack()
        return true
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            //requireActivity().onBackPressed()
        }
    }


}