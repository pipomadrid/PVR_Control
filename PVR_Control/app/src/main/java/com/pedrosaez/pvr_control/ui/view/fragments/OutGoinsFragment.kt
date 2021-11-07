package com.pedrosaez.pvr_control.ui.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.DatosPvr
import com.pedrosaez.pvr_control.database.entities.OutGoins
import com.pedrosaez.pvr_control.databinding.FragmentAddPvrBinding
import com.pedrosaez.pvr_control.databinding.FragmentOutgoinsBinding
import com.pedrosaez.pvr_control.ui.adapter.OutGoinsAdapter
import com.pedrosaez.pvr_control.ui.adapter.PvrAdapter
import com.pedrosaez.pvr_control.ui.viewmodel.AddPvrViewModel
import com.pedrosaez.pvr_control.ui.viewmodel.OutGoinViewModel


class OutGoinsFragment : Fragment() {


    private var _binding:FragmentOutgoinsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapterOutGoins: OutGoinsAdapter
    val model: OutGoinViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding =FragmentOutgoinsBinding.inflate(layoutInflater)

        //obtenemos los datos del Pvr que vamos a usar
        val prefs = requireActivity().getSharedPreferences((getString(R.string.prefs_file)), Context.MODE_PRIVATE)
        val pvrId = prefs.getLong("pvrId", -1)


        //obtenemos la lista de gastos desde la función del viewmodel y creamos el recyclerView
        model.getOutGoinsOfPVr(pvrId).observe(viewLifecycleOwner, { //--> RECIBO NOTIFICACIÓN DE DATOS NUEVOS

            createRecyclerView(it)


           /* for(i in it){
                if(i.pvr.id==pvrId){

                }
            }*/
        })



        return binding.root

    }



    private fun createRecyclerView(outGoinList: List<OutGoins>) {

        mAdapterOutGoins = OutGoinsAdapter(requireContext(), outGoinList as MutableList<OutGoins>)
        val recyclerView = _binding!!.outGoinRecycler
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = mAdapterOutGoins
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }

    }
}