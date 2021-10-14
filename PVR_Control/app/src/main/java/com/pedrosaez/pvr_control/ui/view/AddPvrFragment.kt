package com.pedrosaez.pvr_control.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Delete
import com.pedrosaez.pvr_control.application.App
import com.pedrosaez.pvr_control.data.entities.DatosPvr
import com.pedrosaez.pvr_control.databinding.FragmentAddPvrBinding
import com.pedrosaez.pvr_control.ui.adapter.PvrAdapter
import com.pedrosaez.pvr_control.ui.dialog.AddPvrDialogFragment
import com.pedrosaez.pvr_control.ui.viewmodel.AddPvrViewModel


class AddPvrFragment : Fragment(),UpdateRecyclerView {


    // variables para crear el binding en los fragment
    private var _binding: FragmentAddPvrBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapterProductos: PvrAdapter
    val model: AddPvrViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val db = App.obtenerDatabase()
        _binding = FragmentAddPvrBinding.inflate(layoutInflater)


        val addDialog: AddPvrDialogFragment = AddPvrDialogFragment(this)

        model.getPvr().observe(viewLifecycleOwner, { //--> RECIBO NOTIFICACIÓN DE DATOS NUEVOS
            createRecyclerView(it)

        })



        //al pulsar el floatingButton creamos el dialogo para añadir PVR
        binding.btNewPvr.setOnClickListener {
            addDialog.show(childFragmentManager, "dialog")
        }

        //cerramos sesion y volvemos al login borrando las sharepreferences
        /* binding.btSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val prefs= requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
            val prefsEdit = prefs.edit()
            prefsEdit.clear()
            prefsEdit.apply()
            requireActivity().onBackPressed()
        }*/


        //Todo poner en menu boton de cerrar sesion

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

    override fun delete(pvr: DatosPvr) {
        mAdapterProductos.deletePvr(pvr)
        model.delete(pvr)
    }

    override fun update(pvr: DatosPvr) {
        TODO("Not yet implemented")
    }

    override fun create(pvr: DatosPvr) {
        model.save(pvr)
        mAdapterProductos.createPvr(pvr)
    }

}
