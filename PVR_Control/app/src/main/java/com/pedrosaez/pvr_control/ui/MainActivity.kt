package com.pedrosaez.pvr_control.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.application.App
import com.pedrosaez.pvr_control.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()  {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }


}