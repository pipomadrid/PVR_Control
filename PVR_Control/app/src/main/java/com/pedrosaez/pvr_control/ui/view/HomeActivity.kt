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
import com.pedrosaez.pvr_control.data.AppDatabase
import com.pedrosaez.pvr_control.databinding.ActivityHomeBinding
import com.pedrosaez.pvr_control.ui.adapter.ViewPagerAdapter

class HomeActivity : AppCompatActivity() {

    private lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val pagerAdapter = ViewPagerAdapter(this)
        val viewPager = binding.pager
        val tab_layout= binding.tabLayout
        viewPager.adapter = pagerAdapter



        val tabLayoutMediator = TabLayoutMediator(
            tab_layout,
            viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Mis Pvr"
                    }
                    1-> {
                        tab.text = "totales"
                    }
                }
            })
        tabLayoutMediator.attach()

    }
    override fun onCreateOptionsMenu(menu:Menu): Boolean {
        val inflater :MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_overflow,menu)
        return true
    }

     override fun onOptionsItemSelected(item:MenuItem): Boolean {
        return when (item.itemId){
            R.id.menu_close_session ->{
                FirebaseAuth.getInstance().signOut()
                val prefs= this.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                val prefsEdit = prefs.edit()
                prefsEdit.clear()
                prefsEdit.apply()
                onBackPressed()
                true
            }

            else ->  super.onOptionsItemSelected(item)
        }
    }



}