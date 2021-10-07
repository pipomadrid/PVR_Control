package com.pedrosaez.pvr_control.ui.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.databinding.FragmentAddPvrBinding
import com.pedrosaez.pvr_control.ui.dialog.AddPvrDialogFragment


class AddPvrFragment : Fragment() {


    // variables para crear el binding en los fragment
    private  var _binding: FragmentAddPvrBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentAddPvrBinding.inflate(layoutInflater)


        val addDialog: AddPvrDialogFragment = AddPvrDialogFragment()

        //al pulsar el floatingButton creamos el dialogo para añadir PVR
        binding.btNewPvr.setOnClickListener{
            addDialog.show(parentFragmentManager,"dialog")
        }

        //cerramos sesion y volvemos al login borrando las sharepreferences
        binding.btSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val prefs= requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
            val prefsEdit = prefs.edit()
            prefsEdit.clear()
            prefsEdit.apply()
            requireActivity().onBackPressed()
        }


        //Todo poner en menu boton de cerrar sesion

        return binding.root



    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}