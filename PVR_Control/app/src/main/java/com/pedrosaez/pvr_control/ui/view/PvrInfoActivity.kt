package com.pedrosaez.pvr_control.ui.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.DatosPvr
import com.pedrosaez.pvr_control.databinding.ActivityPvrInfoBinding
import com.pedrosaez.pvr_control.ui.adapter.HomeViewPagerAdapter
import com.pedrosaez.pvr_control.ui.adapter.PvrInfoViewPagerAdapter
import com.pedrosaez.pvr_control.ui.dialog.AddMachineDialog
import com.pedrosaez.pvr_control.ui.view.fragments.MachineFragment
import java.io.Serializable

class PvrInfoActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPvrInfoBinding
    private lateinit var pvr:Serializable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPvrInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        title = "PVR Control"

        //setup
        val pagerAdapter = PvrInfoViewPagerAdapter(this)
        val viewPager = binding.pagerPvrInfo
        val tabLayout= binding.tabLayoutPvrInfo
        viewPager.adapter = pagerAdapter




        // Vinculamos el Tablayout al viewPager
        val tabLayoutMediator = TabLayoutMediator(
            tabLayout,
            viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Registros"
                    }
                    1-> {
                        tab.text = "Resúmen"
                    }
                    2-> {
                        tab.text = "Gastos"
                    }
                    3-> {
                        tab.text = "Máquinas "
                    }
                }
            })
        tabLayoutMediator.attach()

    }
    // Incializa las opciones del menu overflow
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_overflow,menu)
        return true
    }

    // Al pulsar sobre una accion del menu overFlow realizaremos las operaciones  correspondientes
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menu_close_session ->{
                closeSession()
                true
            }

            else ->  super.onOptionsItemSelected(item)
        }
    }

    // funcion para cerrar la sesión de firebase de la app y eliminar las sharedPreferences
    private fun closeSession() {
        FirebaseAuth.getInstance().signOut()
        val prefs= this.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()
        prefsEdit.clear()
        prefsEdit.apply()
        onBackPressed()
    }

    fun getPvr():Serializable{
        return pvr
    }


}