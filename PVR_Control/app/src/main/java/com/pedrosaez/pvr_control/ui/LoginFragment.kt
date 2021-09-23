package com.pedrosaez.pvr_control.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pedrosaez.pvr_control.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    // variable para crear el binding en los fragment
    private lateinit var binding: FragmentLoginBinding

    //variable de tipo interfaz LoginListen para comunicarnos con la mainActivity
    private lateinit var listener:LoginListener

    // Interfaz para comunicarnos con mainActivity
    interface LoginListener {
        fun goRegister(fragment: Fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        val email= binding.etEmailLogin
        val password = binding.etPasswordLogin

        binding.btAccederLogin.setOnClickListener {
            //comprobar que el email y la contraseña son correcta
            //ir a siguiente fragment
        }

        binding.btRegistroLogin.setOnClickListener {

            //al pulsar registro llamamos a la funcion de la interfaz para ir a la pantalla de registro
            listener.goRegister(RegisterFragment())
        }



        return binding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = activity as LoginListener
    }

}