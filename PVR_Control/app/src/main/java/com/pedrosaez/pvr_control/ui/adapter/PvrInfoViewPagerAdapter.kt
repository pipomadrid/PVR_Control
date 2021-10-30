package com.pedrosaez.pvr_control.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pedrosaez.pvr_control.ui.view.fragments.RecordsFragment
import com.pedrosaez.pvr_control.ui.view.fragments.MachineFragment

class PvrInfoViewPagerAdapter  (fm: FragmentActivity): FragmentStateAdapter(fm) {


    //devuelve numero de paginas de viewPager
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        when(position){

            0->return RecordsFragment()
            1->return MachineFragment()
        }
        return RecordsFragment()
    }
}