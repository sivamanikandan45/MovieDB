package com.example.moviedb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
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
        (activity as AppCompatActivity).supportActionBar?.title="Settings"
        (activity as AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val viewModel:ListViewModel by activityViewModels()
        val radioGrp:RadioGroup=view.findViewById(R.id.view_settings)
        radioGrp.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.grid_view->{viewModel.viewType.value=ViewType.GRID
                }
                R.id.list_view->{viewModel.viewType.value=ViewType.LIST
                }
            }
        }

        /*when(viewModel.viewType.value){
            ViewType.LIST->{
                val gridViewBtn=view.findViewById<RadioButton>(R.id.grid_view)
                gridViewBtn.isChecked=false
                val listViewBtn=view.findViewById<RadioButton>(R.id.list_view)
                listViewBtn.isChecked=true
            }
            ViewType.GRID->{
                val listViewBtn=view.findViewById<RadioButton>(R.id.list_view)
                listViewBtn.isChecked=false
                val gridViewBtn=view.findViewById<RadioButton>(R.id.grid_view)
                gridViewBtn.isChecked=true
            }
        }*/

        viewModel.viewType.observe(viewLifecycleOwner, Observer {
            if(it==ViewType.LIST){
                val gridViewBtn=view.findViewById<RadioButton>(R.id.grid_view)
                gridViewBtn.isChecked=false
                val listViewBtn=view.findViewById<RadioButton>(R.id.list_view)
                listViewBtn.isChecked=true
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