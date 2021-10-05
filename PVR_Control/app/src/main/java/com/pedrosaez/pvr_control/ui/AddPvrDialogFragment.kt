package com.pedrosaez.pvr_control.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.application.App
import com.pedrosaez.pvr_control.data.AppDatabase
import com.pedrosaez.pvr_control.data.model.DatosPvr
import com.pedrosaez.pvr_control.databinding.AddDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// Creamos un dialogo que nos va a servir para introducir los datos del PVR desde el homeFragment
class AddPvrDialogFragment:DialogFragment(){


    private var _binding: AddDialogBinding?= null
    // This property is only valid between onCreateDialog and
    // onDestroyView.
    private val binding get() = _binding!!


    ////codigo de prueba, hay que llevarlo al ViewModel
    private val pvr = DatosPvr("El vino","Juan Lopez","c/la hoja 2",65336952)
    val db = App.obtenerDatabase()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
           _binding = AddDialogBinding.inflate(LayoutInflater.from(it))
            val builder = AlertDialog.Builder(it)

            builder.setView(binding.root)
                    .setPositiveButton("Guardar",
                            DialogInterface.OnClickListener { dialog, id ->

                                //codigo de prueba, hay que llevarlo al ViewModel
                                lifecycleScope.launch {
                                    withContext(Dispatchers.IO) {
                                        db.datosPvrDao().save(pvr)
                                    }
                                }

                            })
                    .setNegativeButton("Cancelar",
                            DialogInterface.OnClickListener { dialog, id ->
                                getDialog()?.cancel()
                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}