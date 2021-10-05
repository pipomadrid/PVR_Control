package com.pedrosaez.pvr_control.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pedrosaez.pvr_control.ui.AddPvrFragment
import com.pedrosaez.pvr_control.ui.BlankFragment
import com.pedrosaez.pvr_control.ui.BlankFragment2


//Clase adaptadora para viewpager

class ViewPagerAdapter (fm:FragmentActivity):FragmentStateAdapter(fm) {

    //devuelve numero de paginas de viewPager
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        when(position){

            0->return AddPvrFragment()
            1->return BlankFragment()
            2->return BlankFragment2()
        }
        return AddPvrFragment()
    }


}