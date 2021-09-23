package com.pedrosaez.pvr_control.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() , LoginFragment.LoginListener {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Carga el fragment login en el contenedor del main al iniciar la app
        supportFragmentManager.beginTransaction()
            .add(R.id.contenedorFragment, LoginFragment(),"LoginFragment").commit()

    }

    override fun goRegister(fragment:Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.contenedorFragment,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}