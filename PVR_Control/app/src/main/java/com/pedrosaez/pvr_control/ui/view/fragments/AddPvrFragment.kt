package com.pedrosaez.pvr_control.ui.view.fragments

import android.content.Context
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.DatosPvr
import com.pedrosaez.pvr_control.databinding.FragmentAddPvrBinding
import com.pedrosaez.pvr_control.ui.adapter.PvrAdapter
import com.pedrosaez.pvr_control.ui.dialog.AddPvrDialogFragment
import com.pedrosaez.pvr_control.ui.listeners.PvrModificationListener
import com.pedrosaez.pvr_control.ui.viewmodel.AddPvrViewModel
import com.pedrosaez.pvr_control.ui.viewmodel.UserViewModel


class AddPvrFragment : Fragment(), PvrModificationListener {


    // binding
    private lateinit var actualPvr:DatosPvr
    private var _binding: FragmentAddPvrBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapterProducts: PvrAdapter
    val model: AddPvrViewModel by viewModels()
    val modelUser:UserViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentAddPvrBinding.inflate(layoutInflater)


        val addDialog: AddPvrDialogFragment = AddPvrDialogFragment(this)

        val prefs = requireActivity().getSharedPreferences((getString(R.string.prefs_file)), Context.MODE_PRIVATE)

        val currentUser = FirebaseAuth.getInstance().currentUser
        var userId:Long =0
        var uid = currentUser?.uid

        modelUser.getAllUser.observe(viewLifecycleOwner, {
            for (i in it) {
                if (i.uid ==uid ) {
                    userId = i.id
                    prefs.edit().putLong("userId", userId).apply()
                }
            }
        })

        model.getAll_Pvr.observe(viewLifecycleOwner, { //--> RECIBO NOTIFICACIÓN DE DATOS NUEVOS

            for (i in it) {
                if (i.user.uid == uid) {
                    /* userId= i.user.id*/
                    createRecyclerView(i.DatosPvr)
                }

            }
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

        mAdapterProducts = PvrAdapter(requireContext(), pvr_list as MutableList<DatosPvr>, this)
        val recyclerView = _binding!!.reciclerViewPvr
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = mAdapterProducts
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }

    }

    //obtenemos el pvr actual del recyclerview
    override fun sendActualPvr(pvr: DatosPvr) {
        actualPvr= pvr
    }

    override fun delete(pvr: DatosPvr) {
        model.delete(pvr)
        mAdapterProducts.deletePvr(pvr)
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
            mAdapterProducts.updatePvr(actualPvr)

        }else {
            Snackbar.make(requireView(), getString(R.string.no_data_found_to_update), Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bt_new_pvr)//mostramos en snackbar encima del floating button
                .show()
        }

    }

    override fun create(pvr: DatosPvr) {
        model.save(pvr)
        mAdapterProducts.createPvr(pvr)
    }



}