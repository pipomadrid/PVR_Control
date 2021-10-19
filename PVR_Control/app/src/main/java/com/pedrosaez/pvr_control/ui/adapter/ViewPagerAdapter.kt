package com.pedrosaez.pvr_control.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pedrosaez.pvr_control.ui.view.fragments.AddPvrFragment
import com.pedrosaez.pvr_control.ui.view.fragments.BlankFragment


//Clase adaptadora para viewpager

class ViewPagerAdapter (fm:FragmentActivity):FragmentStateAdapter(fm) {

    //devuelve numero de paginas de viewPager
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        when(position){

            0->return AddPvrFragment()
            1->return BlankFragment()
        }
        return AddPvrFragment()
    }


}