package com.pedrosaez.pvr_control.ui.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.pedrosaez.pvr_control.data.entities.DatosPvr
import com.pedrosaez.pvr_control.databinding.AddDialogBinding
import com.pedrosaez.pvr_control.ui.viewmodel.AddPvrViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.SimpleFormatter


// Creamos un dialogo que nos va a servir para introducir los datos del PVR desde el homeFragment
class AddPvrDialogFragment:DialogFragment(){


    private var _binding: AddDialogBinding?= null
    private val binding get() = _binding!!


    private lateinit var pvr:DatosPvr


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
           _binding = AddDialogBinding.inflate(LayoutInflater.from(it))
            val builder = AlertDialog.Builder(it)

            //obtenemos los valores de los campos


            builder.setView(binding.root)

            val address = binding.etAddress.text
            val pvrName = binding.etPvrName.text
            val phone = binding.etTelephone.text
            val nameSurname= binding.etNameAndSurname.text
            val authDate = binding.etAuthDate
            val model:AddPvrViewModel by viewModels()
            val calendar = Calendar.getInstance()

            authDate.setOnClickListener {
                showDatePickerDialog(authDate,calendar)
            }

            builder.setPositiveButton("Guardar",
                            DialogInterface.OnClickListener { dialog, id ->
                                if(!nameSurname.isNullOrEmpty() && !pvrName.isNullOrEmpty()){
                                    pvr= DatosPvr(pvrName.toString(),nameSurname.toString(),address.toString(),phone.toString().toInt(),calendar.time)
                                    model.save(pvr)
                                }else{
                                    Toast.makeText(requireContext(),"Los campos nombre y datos del titular no pueden estar vacíos",Toast.LENGTH_SHORT).show()
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