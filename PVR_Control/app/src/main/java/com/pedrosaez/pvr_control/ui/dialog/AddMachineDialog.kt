package com.pedrosaez.pvr_control.ui.dialog

import android.app.AlertDialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.PvrMachine
import com.pedrosaez.pvr_control.databinding.MachineDialogBinding
import com.pedrosaez.pvr_control.ui.view.fragments.MachineFragment


// Dialogo paraa introducir datos de la maquina expendedora
class AddMachineDialog(val machineListener: MachineAddListener):DialogFragment() {


    private var _binding: MachineDialogBinding?= null
    private val binding get() = _binding!!

    private lateinit var  pvrMachine : PvrMachine


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            _binding = MachineDialogBinding.inflate(LayoutInflater.from(it))
            val builder = AlertDialog.Builder(it)

            builder.setView(binding.root)

            //setup
            val brand = binding.etBrand
            val modelMachine = binding.etModel
            val serialNumber = binding.etSerialNumber
            val railsNumber = binding.etRailsNumber

            val viewParentfragment: View? = parentFragment?.requireView()
            val addButton = binding.btAddRail
            val minusButton = binding.btMinusRail

            railsNumber.setText("0")
            var rails = 0

            // boton aumentar numero carriles carriles
            addButton.setOnClickListener{
                rails +=1
                if(rails<=50) {
                    railsNumber.setText(rails.toString())
                }
            }
            //boton  disminuir numero carriles
            minusButton.setOnClickListener{
                rails -=1
                if(rails>=0) {
                    railsNumber.setText(rails.toString())
                }
            }
            
            builder.setPositiveButton("Guardar",
                    DialogInterface.OnClickListener { dialog, id ->
                        //obtenemmos los datos introducidos en los edittext
                        val brandString = brand.text?.toString() ?: " "
                        val modelMachineString = modelMachine.text?.toString() ?: " "
                        val serialNumberString = serialNumber.text?.toString() ?: " "
                        val railsNumberInt = railsNumber.text.toString()

                        val prefs = requireActivity().getSharedPreferences((getString(R.string.prefs_file)), Context.MODE_PRIVATE)
                        val pvrId: Long = prefs.getLong("pvrId", -1)

                        pvrMachine = PvrMachine(brandString, modelMachineString, serialNumberString, railsNumberInt.toInt(), pvrId)


                        if ( tag =="addMachine") {
                            if (brandString.isNotEmpty() && modelMachineString.isNotEmpty() && serialNumberString.isNotEmpty() && railsNumberInt.isNotEmpty()) {

                                machineListener.onPositiveClick()
                                machineListener.saveMachine(pvrMachine)

                            } else {
                                Snackbar.make(viewParentfragment!!, getString(R.string.must_fill_fields), Snackbar.LENGTH_LONG)
                                        .setAnchorView(R.id.bt_new_machine)
                                        .show()
                            }
                        }else { // si el tag es updateMachine
                            machineListener.updateMachine(pvrMachine)
                        }



                    })
            builder.setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface MachineAddListener {

        fun onPositiveClick()

        fun saveMachine(machine:PvrMachine)

        fun updateMachine(machine: PvrMachine)


    }

}