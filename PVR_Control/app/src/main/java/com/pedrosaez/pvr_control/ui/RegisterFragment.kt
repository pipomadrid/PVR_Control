package com.pedrosaez.pvr_control.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pedrosaez.pvr_control.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private val TAG: String? = "LoginFragment"

    // variables para crear el binding en los fragment
    private lateinit var binding: FragmentRegisterBinding

    //variable para uso de firebase
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        val email = binding.etEmailRegister
        val password = binding.etPasswordRegister
        val botonRegistro = binding.btRegistrate

        auth= Firebase.auth




        botonRegistro.setOnClickListener {
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    /* task son tareas asincronas , podemos obtener informacion sobre su estado y tienen
                listeners que nos permiten notificar que ha cambiado el estado */
                    .addOnCompleteListener(activity as MainActivity) { task ->
                        if (task.isSuccessful) {
                            (activity as MainActivity).finish()

                        } else {
                            Log.d(TAG, task.exception.toString())
                        }
                    }
        }

        return binding.root
    }

}