package com.pedrosaez.pvr_control.ui.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.DatosPvr
import com.pedrosaez.pvr_control.databinding.AddDialogBinding
import com.pedrosaez.pvr_control.ui.listeners.PvrModificationListener
import com.pedrosaez.pvr_control.ui.viewmodel.AddPvrViewModel
import java.util.*


// Creamos un dialogo que nos va a servir para introducir los datos del PVR desde el homeFragment
class AddPvrDialogFragment(val updateRecyclerView: PvrModificationListener):DialogFragment()
{


    private var _binding: AddDialogBinding?= null
    private val binding get() = _binding!!


    private lateinit var pvr:DatosPvr


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
           _binding = AddDialogBinding.inflate(LayoutInflater.from(it))
            val builder = AlertDialog.Builder(it)

            builder.setView(binding.root)

            //setup
            val address = binding.etAddress
            val pvrName = binding.etPvrName
            val phone = binding.etTelephone
            val nameSurname = binding.etNameAndSurname
            val authDate = binding.etAuthDate
            val viewParentfragment: View? = parentFragment?.requireView()
            val calendar: Calendar = Calendar.getInstance()


            val model: AddPvrViewModel by viewModels()

            authDate.setOnClickListener {
                showDatePickerDialog(authDate, calendar)
            }



            builder.setPositiveButton(

                    if (tag == "AddDialog") {
                        "Guardar"
                    } else "Actualizar",

                    DialogInterface.OnClickListener { dialog, id ->

                        //tomamos los valores de los editText y los asignamos
                        val addressPvr = address.text?.toString() ?: " "
                        val phonePvr = phone.text?.toString() ?: " "
                        var calendarPvr: Date? = calendar.time
                        val authDateString = binding.etAuthDate.text.toString()
                        if (authDateString.isEmpty()) {
                            calendarPvr = null
                        }
                        // creamos un nuevo Pvr con los datos introducidos en el Dialog para crear o actualizar según el caso
                        pvr = DatosPvr(pvrName.text.toString(), nameSurname.text.toString(), addressPvr, phonePvr, calendarPvr)

                        if (tag == "AddDialog") {
                            if (nameSurname.text.toString().isNotEmpty() && pvrName.text.toString().isNotEmpty()) {

                                updateRecyclerView.create(pvr)

                            } else {
                                // Al crear un nuevo Pvr nos aseguramos de que los campos nombre y datos del titular no esten vacios
                                Snackbar.make(viewParentfragment!!, getString(R.string.fields_cant_be_empty), Snackbar.LENGTH_LONG)
                                        .setAnchorView(R.id.bt_new_pvr)
                                        .show()
                            }

                        } else {
                            //Cuando el tag es actualizar  llamamos al metodo update
                            updateRecyclerView.update(pvr)
                        }


                    })
            builder.setNegativeButton("Cancelar",
                            DialogInterface.OnClickListener { dialog, id ->
                                getDialog()?.cancel()
                            })
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    //Funcion para mostrar el DatePickerDialog
    private fun showDatePickerDialog(editText:EditText,calendar:Calendar){

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

//Funcion de extensión de la clase Int para mostrar dos dígitos
fun Int.twoDigits() = if (this <= 9) "0$this" else this.toString()