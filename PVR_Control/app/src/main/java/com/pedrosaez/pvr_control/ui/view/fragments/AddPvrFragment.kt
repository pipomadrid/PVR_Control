package com.pedrosaez.pvr_control.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.application.App
import com.pedrosaez.pvr_control.database.entities.DatosPvr
import com.pedrosaez.pvr_control.databinding.FragmentAddPvrBinding
import com.pedrosaez.pvr_control.ui.adapter.PvrAdapter
import com.pedrosaez.pvr_control.ui.dialog.AddPvrDialogFragment
import com.pedrosaez.pvr_control.ui.listeners.PvrModificationListener
import com.pedrosaez.pvr_control.ui.viewmodel.AddPvrViewModel


class AddPvrFragment : Fragment(), PvrModificationListener {


    // variables para crear el binding en los fragment
    private lateinit var actualPvr:DatosPvr
    private var _binding: FragmentAddPvrBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapterProductos: PvrAdapter
    val model: AddPvrViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val db = App.obtenerDatabase()
        _binding = FragmentAddPvrBinding.inflate(layoutInflater)


        val addDialog: AddPvrDialogFragment = AddPvrDialogFragment(this)



        model.getAll_Pvr.observe(viewLifecycleOwner, { //--> RECIBO NOTIFICACIÓN DE DATOS NUEVOS
            createRecyclerView(it)
        })



        //al pulsar el floatingButton creamos el dialogo para añadir PVR
        binding.btNewPvr.setOnClickListener {
            addDialog.show(childFragmentManager, "AddDialog")
        }


        return binding.root


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createRecyclerView(pvr_list: List<DatosPvr>) {

        mAdapterProductos = PvrAdapter(requireContext(), pvr_list as MutableList<DatosPvr>,this)
        val recyclerView = _binding!!.reciclerViewPvr
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = mAdapterProductos
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }

    }

    //obtenemos el pvr actual del recyclerview
    override fun sendActualPvr(pvr: DatosPvr) {
        actualPvr= pvr
    }

    override fun delete(pvr: DatosPvr) {
        model.delete(pvr)
        mAdapterProductos.deletePvr(pvr)
    }

    override fun update(pvr: DatosPvr) {

        var updateSomeField =false

        //comprobamos los campos que se van a actualizar
        if(pvr.pvrName.isNotEmpty()){
            actualPvr.pvrName = pvr.pvrName
            updateSomeField =true

        }
        if(pvr.nameSurname.isNotEmpty()){
            actualPvr.nameSurname = pvr.nameSurname
            updateSomeField =true
        }
        if(pvr.phone.isNotEmpty()){
            actualPvr.phone = pvr.phone
            updateSomeField =true
        }

        if(pvr.address.isNotEmpty()){
            actualPvr.address = pvr.address
            updateSomeField =true
        }
        if(pvr.authDate != null){
            actualPvr.authDate = pvr.authDate
            updateSomeField =true
        }

        //Si todos los campos están vacios se muestra el snackbar, si hay datos se actualiza el PVR con los mismos
        if(updateSomeField) {
            model.update(actualPvr)
            mAdapterProductos.updatePvr(actualPvr)

        }else {
            Snackbar.make(requireView(), getString(R.string.no_data_found_to_update), Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bt_new_pvr)//mostramos en snackbar encima del floating button
                .show()
        }

    }

    override fun create(pvr: DatosPvr) {
        model.save(pvr)
        mAdapterProductos.createPvr(pvr)
    }



}