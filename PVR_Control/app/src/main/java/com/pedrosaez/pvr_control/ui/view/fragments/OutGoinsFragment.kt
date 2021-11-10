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
import com.google.android.material.snackbar.Snackbar
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.OutGoins
import com.pedrosaez.pvr_control.databinding.FragmentOutgoinsBinding
import com.pedrosaez.pvr_control.ui.adapter.OutGoinsAdapter
import com.pedrosaez.pvr_control.ui.dialog.AddOutGoingDialog
import com.pedrosaez.pvr_control.ui.dialog.AddPvrDialogFragment
import com.pedrosaez.pvr_control.ui.listeners.OutGoingModificationListener
import com.pedrosaez.pvr_control.ui.viewmodel.OutGoinViewModel
import java.util.*


class OutGoinsFragment : Fragment(),OutGoingModificationListener{



    //binding
    private var _binding:FragmentOutgoinsBinding? = null
    private val binding get() = _binding!!


    private lateinit var actualOutGoing:OutGoins
    private lateinit var mAdapterOutGoins: OutGoinsAdapter
    val model: OutGoinViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentOutgoinsBinding.inflate(layoutInflater)


        val addDialog: AddOutGoingDialog = AddOutGoingDialog(this)
        //obtenemos los datos del Pvr que vamos a usar
        val prefs = requireActivity().getSharedPreferences((getString(R.string.prefs_file)), Context.MODE_PRIVATE)
        val pvrId = prefs.getLong("pvrId", -1)


        //obtenemos la lista de gastos desde la función del viewmodel y creamos el recyclerView
        model.getAllOutGoins.observe(viewLifecycleOwner, {pvrAndOutGoins->

            for(pvrAndOut in pvrAndOutGoins) {
                if (pvrAndOut.pvr.id == pvrId) {
                        createRecyclerView(pvrAndOut.outGoins)
                    }

            }


        })


        binding.btNewOutgoing.setOnClickListener {
            addDialog.show(childFragmentManager,"addOutGoin")


        }

        return binding.root

    }



    private fun createRecyclerView(outGoinList: List<OutGoins>) {
        //cambio el orden de la lista pra que muestre el ultimo gasto introducido

     /*   if (outGoinList.isNotEmpty()) {
            val outGoingreverseList = outGoinList.reversed()*/

          /*  mAdapterOutGoins = OutGoinsAdapter(requireContext(), outGoingreverseList as MutableList<OutGoins>, this)*/

            mAdapterOutGoins = OutGoinsAdapter(requireContext(), outGoinList as MutableList<OutGoins>, this)

        val recyclerView = _binding!!.outGoinRecycler
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = mAdapterOutGoins
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }


    }
    override fun sendActualOutGoing(outGoins: OutGoins) {
        actualOutGoing = outGoins
    }


    override fun delete(outGoins: OutGoins) {
        model.deleteOutGoin(outGoins)
        mAdapterOutGoins.deleteOutGoing(outGoins)
    }

    override fun update(outGoins: OutGoins) {
        var updateSomeField =false

        //comprobamos los campos que se van a actualizar
        if(outGoins.cost > 0){
            actualOutGoing.cost = outGoins.cost
            updateSomeField =true

        }
        if(outGoins.date > Date(0)){
            actualOutGoing.date = outGoins.date
            updateSomeField =true
        }
        if(outGoins.description.isNotEmpty()){
            actualOutGoing.description = outGoins.description
            updateSomeField =true
        }

        //Si todos los campos están vacios se muestra el snackbar, si hay datos se actualiza el PVR con los mismos
        if(updateSomeField) {
            model.updateOutGoin(actualOutGoing)
            mAdapterOutGoins.updateOutGoing(actualOutGoing)

        }else {
            Snackbar.make(requireView(), getString(R.string.no_data_found_to_update), Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.bt_new_outgoing)//mostramos en snackbar encima del floating button
                    .show()
        }

    }

    override fun create(outGoins: OutGoins) {
        model.saveOutGoin(outGoins)
        mAdapterOutGoins.createOutgoing(outGoins)

    }


}