package com.example.moviedb

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(HomeFragment())
        val bottomNavigationView:BottomNavigationView=findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home->replaceFragment(HomeFragment())
                R.id.settings->replaceFragment(SettingsFragment())
                R.id.review->replaceFragment(ReviewFragment())
                else->{}
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragmentContainerView,fragment)
        }
    }

    /*override fun onResume() {
        super.onResume()
        println("Main oda resume pa idhu")
    }

    override fun onPause() {
        super.onPause()
        println("Pausing.....")
    }*/

}