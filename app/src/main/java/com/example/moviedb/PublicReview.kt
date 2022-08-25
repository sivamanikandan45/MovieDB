package com.example.moviedb

import android.net.Uri

data class PublicReview(var avatar: Uri,var name:String,var account:String,var comment: String,var rating:String,var isVisible:Boolean=false)
