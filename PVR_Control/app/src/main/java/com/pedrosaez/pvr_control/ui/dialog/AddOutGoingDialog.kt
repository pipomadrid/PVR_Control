package com.pedrosaez.pvr_control.ui.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.OutGoins
import com.pedrosaez.pvr_control.databinding.OutGoinDialogBinding
import com.pedrosaez.pvr_control.ui.listeners.OutGoingModificationListener
import com.pedrosaez.pvr_control.ui.viewmodel.OutGoinViewModel
import java.util.*

class AddOutGoingDialog( val outGoingListener:OutGoingModificationListener):DialogFragment() {


    //binding
    private var _binding: OutGoinDialogBinding?= null
    private val binding get() = _binding!!


    private lateinit var outGoing:OutGoins
    private lateinit var lastOutGoing:OutGoins
    private var _pvrId: Long? = null
    private val model: OutGoinViewModel by activityViewModels()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            _binding = OutGoinDialogBinding.inflate(LayoutInflater.from(it))
            val builder = AlertDialog.Builder(it)

            builder.setView(binding.root)

            //setup
            val etAmount = binding.etAmount
            val etDate = binding.etDate
            val etConcept = binding.etConcept
            val viewParentfragment: View? = parentFragment?.requireView()
            val calendar: Calendar = Calendar.getInstance()





            etDate.setOnClickListener {
                showDatePickerDialog(etDate, calendar)
            }

            // obtenemos el Id del pvr
            val prefs = requireActivity().getSharedPreferences((getString(R.string.prefs_file)), Context.MODE_PRIVATE)
            _pvrId = prefs.getLong("pvrId", -1)

            model.getOutGoinsOfPVr(_pvrId!!).observe(this,{outGoingList ->

                if(outGoingList.isNotEmpty()) {
                    lastOutGoing = outGoingList.last()
                }
        })

            builder.setPositiveButton(

                    if (tag == "addOutGoin") {
                        "Guardar"
                    } else "Actualizar",
                    DialogInterface.OnClickListener { dialog, id ->

                        //obtenemmos los datos introducidos en los edittext
                        val amount = etAmount.text?.toString() ?: " "
                        val concept = etConcept.text?.toString() ?:" "
                        var calendar = calendar.time
                        val date = binding.etDate.text.toString()
                        if(date.isEmpty()) {
                            calendar = null
                        }

                        // Creamos un gasto asociado a un PVR
                        outGoing = OutGoins(amount.toInt(),concept,calendar, _pvrId!!)


                        if (tag == "addOutGoin") {
                            if (amount.isNotEmpty() && concept.isNotEmpty() && date.isNotEmpty()) {

                               /* machineListener.onPositiveClick()*/
                               outGoingListener.create(outGoing)

                            } else {
                                Snackbar.make(viewParentfragment!!, getString(R.string.must_fill_fields), Snackbar.LENGTH_LONG)
                                        .setAnchorView(R.id.bt_new_outgoing)
                                        .show()
                            }
                        } else { // si el tag es updateMachine

                            outGoingListener.update(outGoing)

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


    //Funcion para mostrar el DatePickerDialog
    private fun showDatePickerDialog(editText: EditText, calendar:Calendar){

        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { picker, year, month, day ->
            val selectedDateString = day.twoDigits() + " / " + ((month + 1).twoDigits()) + " / " + year
            editText.setText(selectedDateString)
            val day = picker.dayOfMonth
            val month = picker.month
            val year = picker.year
            calendar.set(year,month,day)
        })
        activity?.let { newFragment.show(it.supportFragmentManager, "datePicker") }
    }



}

