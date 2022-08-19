package com.example.moviedb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel:ListViewModel by activityViewModels()
        val radioGrp:RadioGroup=view.findViewById(R.id.view_settings)
        radioGrp.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.grid_view->{viewModel.viewType.value=ViewType.GRID
                println("Grid view is selected")}
                R.id.list_view->{viewModel.viewType.value=ViewType.LIST
                    println("List view is selected")}
            }
        }

        viewModel.viewType.observe(viewLifecycleOwner, Observer {
            if(it==ViewType.LIST){
                val listViewBtn=view.findViewById<RadioButton>(R.id.list_view)
                listViewBtn.isChecked=true
                val gridViewBtn=view.findViewById<RadioButton>(R.id.grid_view)
                gridViewBtn.isChecked=false
            }
            else if(it==ViewType.GRID){
                val listViewBtn=view.findViewById<RadioButton>(R.id.list_view)
                listViewBtn.isChecked=false
                val gridViewBtn=view.findViewById<RadioButton>(R.id.grid_view)
                gridViewBtn.isChecked=true
            }
        })
    }
}