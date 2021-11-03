package com.pedrosaez.pvr_control.ui.dialog

import android.app.AlertDialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.PvrMachine
import com.pedrosaez.pvr_control.databinding.MachineDialogBinding
import com.pedrosaez.pvr_control.ui.viewmodel.MachineViewModel


// Dialogo paraa introducir datos de la maquina expendedora
class AddMachineDialog(val machineListener: MachineDialogListener):DialogFragment() {


    private var _binding: MachineDialogBinding?= null
    private val binding get() = _binding!!

    private lateinit var  pvrMachine : PvrMachine
    private var _pvrId: Long? = null
    private val model: MachineViewModel by activityViewModels()


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

            // obtenemos el Id del pvr
            val prefs = requireActivity().getSharedPreferences((getString(R.string.prefs_file)), Context.MODE_PRIVATE)
            _pvrId = prefs.getLong("pvrId", -1)

            builder.setPositiveButton(
                    if (tag == "addMachine") {
                        "Guardar"
                    } else "Actualizar",
                    DialogInterface.OnClickListener { dialog, id ->

                        //obtenemmos los datos introducidos en los edittext
                        val brandString = brand.text?.toString() ?: " "
                        val modelMachineString = modelMachine.text?.toString() ?: " "
                        val serialNumberString = serialNumber.text?.toString() ?: " "
                        val railsNumberInt = railsNumber.text.toString()

                        // Creamos una nueva maquina con los datos del Pvr
                        pvrMachine = PvrMachine(brandString, modelMachineString, serialNumberString, railsNumberInt.toInt(), _pvrId!!)


                        if (tag == "addMachine") {
                            if (brandString.isNotEmpty() && modelMachineString.isNotEmpty() && serialNumberString.isNotEmpty() && railsNumberInt.isNotEmpty()) {

                                machineListener.onPositiveClick()
                                model.save(pvrMachine)

                            } else {
                                Snackbar.make(viewParentfragment!!, getString(R.string.must_fill_fields), Snackbar.LENGTH_LONG)
                                        .setAnchorView(R.id.bt_new_machine)
                                        .show()
                            }
                        } else { // si el tag es updateMachine
                            machineListener.onPositiveClick()
                            if (!checkAndUpdateData(pvrMachine)) {
                                Snackbar.make(viewParentfragment!!, getString(R.string.no_data_found_to_update), Snackbar.LENGTH_LONG)
                                        .setAnchorView(R.id.bt_new_machine)//mostramos en snackbar encima del floating button
                                        .show()
                            }

                        }
                        //Si todos los campos están vacios se muestra el snackbar, si hay datos se actualiza el PVR con los mismos


                    })
            builder.setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }


    fun checkAndUpdateData (updateMachine :PvrMachine):Boolean{

        val machineAndPvrs: List<PvrMachine>? = model.getMachine.value
        var actualMachine:PvrMachine?=null

        if (machineAndPvrs != null) {
            for (i in machineAndPvrs) {

                if (i.pvrId == _pvrId) {
                    actualMachine = i
                }
            }
        }
        var updateSomeField = false

        //comprobamos los campos que se van a actualizar
        if (updateMachine.brand.isNotEmpty()) {
            actualMachine!!.brand = updateMachine.brand
            updateSomeField = true
        }
        if (updateMachine.model.isNotEmpty()) {
            actualMachine!!.model = updateMachine.model
            updateSomeField = true
        }
        if (updateMachine.serialNumber.isNotEmpty()) {
            actualMachine!!.serialNumber = updateMachine.serialNumber
            updateSomeField = true
        }
        if (updateMachine.railsNumber !=0) {
            actualMachine!!.railsNumber = updateMachine.railsNumber
            updateSomeField = true
        }
        if (updateSomeField) {
            model.update(actualMachine!!)

        }


        return updateSomeField
    }

    interface MachineDialogListener {

        fun onPositiveClick()

    }



}