package com.pedrosaez.pvr_control.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pedrosaez.pvr_control.databinding.FragmentLoginBinding
import com.pedrosaez.pvr_control.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    // variables para crear el binding en los fragment
    private lateinit var binding: FragmentRegisterBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)


        return binding.root
    }
}