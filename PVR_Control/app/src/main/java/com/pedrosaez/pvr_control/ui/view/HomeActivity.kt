package com.pedrosaez.pvr_control.ui.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.databinding.ActivityHomeBinding
import com.pedrosaez.pvr_control.ui.adapter.HomeViewPagerAdapter

class HomeActivity : AppCompatActivity() {

    private lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title = "PVR Control"

        //setup
        val pagerAdapter = HomeViewPagerAdapter(this)
        val viewPager = binding.pager
        val tab_layout= binding.tabLayout
        viewPager.adapter = pagerAdapter



        // Vinculamos el Tablayout al viewPager
        val tabLayoutMediator = TabLayoutMediator(
            tab_layout,
            viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Mis Pvr"
                    }
                    1-> {
                        tab.text = "Informes"
                    }
                }
            })
        tabLayoutMediator.attach()

    }
    // Incializa las opciones del menu
    override fun onCreateOptionsMenu(menu:Menu): Boolean {
        val inflater :MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_overflow,menu)
        return true
    }

    // Al pulsar sobre una accion del menu overFlow realizaremos las operaciones  correspondientes
     override fun onOptionsItemSelected(item:MenuItem): Boolean {
        return when (item.itemId){
            R.id.menu_close_session ->{
                closeSession()
                true
            }
            R.id.menu_get_out ->{
                finishAffinity()
                true
            }

            else ->  super.onOptionsItemSelected(item)
        }
    }

    // funcion para cerrar la sesión de firebase de la app eliminar las sharedPreferences y volver al login
    private fun closeSession() {
        FirebaseAuth.getInstance().signOut()
        val prefs= this.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()
        prefsEdit.clear()
        prefsEdit.apply()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    // anulamos la accion para que el usuario no pueda volver a atras desde el  boton
    override fun  onBackPressed() {


    }


}