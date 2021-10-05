package com.pedrosaez.pvr_control.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
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
                    1 -> {
                        tab.text = "ventas"
                    }
                    2 -> {
                        tab.text = "totales"
                    }
                }
            })
        tabLayoutMediator.attach()

    }
}