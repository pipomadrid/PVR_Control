package com.pedrosaez.pvr_control.ui.adapter

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pedrosaez.pvr_control.ui.view.HomeActivity
import com.pedrosaez.pvr_control.ui.view.fragments.OutGoinsFragment
import com.pedrosaez.pvr_control.ui.view.fragments.RecordsFragment
import com.pedrosaez.pvr_control.ui.view.fragments.MachineFragment
import com.pedrosaez.pvr_control.ui.view.fragments.SalesSummaryFragment

class PvrInfoViewPagerAdapter  (fm: FragmentActivity): FragmentStateAdapter(fm) {


    //devuelve numero de paginas de viewPager
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        when(position){

            0->return RecordsFragment()
            1->return SalesSummaryFragment()
            2->return OutGoinsFragment()
            3->return MachineFragment()

        }
        return RecordsFragment()
    }


}