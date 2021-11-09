package com.pedrosaez.pvr_control.ui.view.fragments

import android.R.*
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.pedrosaez.pvr_control.database.entities.OutGoins
import com.pedrosaez.pvr_control.database.entities.TotalRecords
import com.pedrosaez.pvr_control.databinding.FragmentReportBinding
import com.pedrosaez.pvr_control.ui.dialog.DatePickerFragment
import com.pedrosaez.pvr_control.ui.dialog.twoDigits
import com.pedrosaez.pvr_control.ui.viewmodel.AddPvrViewModel
import com.pedrosaez.pvr_control.ui.viewmodel.OutGoinViewModel
import com.pedrosaez.pvr_control.ui.viewmodel.TotalRecordViewModel
import java.util.*

class ReportFragment : Fragment(){
    // TODO: Rename and change types of parameters


    //Binding
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

    //viewModel
    val modelPvr: AddPvrViewModel  by activityViewModels()
    val modelTotals:TotalRecordViewModel  by activityViewModels()
    val modelOutGoing:OutGoinViewModel  by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {


        _binding = FragmentReportBinding.inflate(layoutInflater)

        //setup
        val pvrSpinner = binding.spinner
        val etInitialDate = binding.etInicialDate
        val etFinalDate = binding.etFinalDate
        val btSend = binding.btSend
        val tvSales = binding.tvReportSales
        val tvGains = binding.tvReportGains
        val tvOutGoins =  binding.tvReportOutgoins
        val reportInitCalendar: Calendar = Calendar.getInstance()
        val reportFinishCalendar: Calendar = Calendar.getInstance()

        //obtenemos el uid del usuario
        val currentUser = FirebaseAuth.getInstance().currentUser
        var userId:Long =0
        var uid = currentUser?.uid


        var _pvrName = ""
        var pvrList:MutableList<String> = emptyList<String>().toMutableList()

        // creamos el adpatador para el spinner y se lo asignamos
        val spinnerAdapter  = ArrayAdapter(requireContext(), layout.simple_spinner_dropdown_item,pvrList)
        pvrSpinner.adapter = spinnerAdapter

        //observamos los datos de los pvr para actualizar el spinner
        modelPvr.getAll_Pvr.observe(viewLifecycleOwner, {
            pvrList.clear()
            pvrList.add("Totales")
            pvrSpinner.setSelection(0);
            for (i in it) {
                if (i.user.uid == uid) {
                    for (pvr in i.DatosPvr) {
                        val pvrName = pvr.pvrName
                        pvrList.add(pvr.pvrName)
                        spinnerAdapter.notifyDataSetChanged()
                        _pvrName = pvrSpinner.selectedItem as String
                    }
                }
            }

        })



        val onePvrTotalList =  mutableListOf<TotalRecords>()
        val onePvrOutGoingList=  mutableListOf<OutGoins>()
        val allPvrTotalList =  mutableListOf<TotalRecords>()
        val allPvrOutGoingList=  mutableListOf<OutGoins>()

        modelTotals.getTotalRecords.observe(viewLifecycleOwner, {

            onePvrtotalSales = 0
            // obtenemos los totales de el Pvr escogido en el spinner
            for (i in it) {
                for(totals in i.totalRecords){
                    //obtenemos los totales de todos los PVR
                    allPvrTotalList.add(totals)
                }
                if (_pvrName == i.pvr.pvrName) {
                    for (total in i.totalRecords) {
                        //Obtenemos los totales del Pvr elegido
                       onePvrTotalList.add(total)
                    }
                }
            }


        })

        modelOutGoing.getAllOutGoins.observe(viewLifecycleOwner, {

            onePvrTotalOutgoings = 0.0
            // obtenemos los totales de el Pvr escogido en el spinner
            for (i in it) {
                for(outGoing in i.outGoins){
                    //obtenemos todos los gastos de todos los pvr
                    allPvrOutGoingList.add(outGoing)
                }
                if (_pvrName == i.pvr.pvrName) {
                    for (outGoing in i.outGoins) {
                        //obtenemos todos los gastos del pvr elegido
                        onePvrOutGoingList.add(outGoing)
                    }
                }
            }

        })

        btSend.setOnClickListener {
          if(_pvrName.equals("Totales")){
              if(etInitialDate.text.isNotEmpty() && etFinalDate.text.isNullOrEmpty()){

                  for(outgoing in allPvrOutGoingList){
                      if(outgoing.date >= reportInitCalendar.time){
                          allPvrTotalOutGoings += outgoing.cost

                      }
                  }
                  for(total in allPvrTotalList){
                      if(total.createAt >= reportInitCalendar.time){
                          allPvrTotalSales += total.sells
                          allPvrtotalGains += (total.money * USER_COMISION)-allPvrTotalOutGoings
                      }
                  }

                  tvOutGoins.text = allPvrTotalOutGoings.toString()
                  tvSales.text = allPvrTotalSales.toString()
                  tvGains.text = allPvrtotalGains.toString()

              }else if(etInitialDate.text.isNullOrEmpty() && etFinalDate.text.isNotEmpty()){
                  for(outgoing in allPvrOutGoingList){
                      if(outgoing.date <= reportFinishCalendar.time){
                          allPvrTotalOutGoings += outgoing.cost

                      }
                  }
                  for(total in allPvrTotalList){
                      if(total.createAt >= reportFinishCalendar.time){
                          allPvrTotalSales += total.sells
                          allPvrtotalGains += (total.money * USER_COMISION)-allPvrTotalOutGoings
                      }
                  }
                  tvOutGoins.text = allPvrTotalOutGoings.toString()
                  tvSales.text = allPvrTotalSales.toString()
                  tvGains.text = allPvrtotalGains.toString()

              }else if(etInitialDate.text.isNullOrEmpty() && etFinalDate.text.isNullOrEmpty()){
                  for(outgoing in allPvrOutGoingList){
                      allPvrTotalOutGoings += outgoing.cost
                  }
                  for (total in allPvrTotalList) {

                      allPvrTotalSales += total.sells
                      allPvrtotalGains += (total.money * USER_COMISION) - allPvrTotalOutGoings
                  }
                  tvOutGoins.text = allPvrTotalOutGoings.toString()
                  tvSales.text = allPvrTotalSales.toString()
                  tvGains.text = allPvrtotalGains.toString()

              }else {
                  for(outgoing in allPvrOutGoingList){
                      if(outgoing.date >=reportInitCalendar.time && outgoing.date <= reportFinishCalendar.time){
                          allPvrTotalOutGoings += outgoing.cost

                      }
                  }
                  for(total in allPvrTotalList){
                      if(total.createAt >=reportInitCalendar.time && total.createAt <= reportFinishCalendar.time){
                          allPvrTotalSales += total.sells
                          allPvrtotalGains += (total.money * USER_COMISION)-allPvrTotalOutGoings
                      }
                  }
                  tvOutGoins.text = allPvrTotalOutGoings.toString()
                  tvSales.text = allPvrTotalSales.toString()
                  tvGains.text = allPvrtotalGains.toString()

              }

          }

        }



        etInitialDate.setOnClickListener {
            showDatePickerDialog(etInitialDate,reportInitCalendar)

        }

        etFinalDate.setOnClickListener {
            showDatePickerDialog(etFinalDate,reportFinishCalendar)

        }




        return  binding.root


    }

    private fun showDatePickerDialog(editText: EditText, calendar: Calendar){

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


    private fun checkReport(totalList:MutableList<TotalRecords>,outgoingList:MutableList<OutGoins>,
                            tvSales:TextView,tvOutGoins:TextView,
                            tvGains:TextView,initDate:Calendar,finishDate:Calendar,etInitialDate:EditText,etFinalDate:EditText){

        var onePvrtotalSales = 0L
        var allPvrTotalSales = 0L
        var onePvrtotalGains = 0.0
        var allPvrtotalGains = 0.0
        var onePvrTotalOutgoings: Double
        var allPvrTotalOutGoings = 0.0

        if(etInitialDate.text.isNotEmpty() && etFinalDate.text.isNullOrEmpty()){

            for(outgoing in outgoingList){
                if(outgoing.date >= initDate.time){
                    allPvrTotalOutGoings += outgoing.cost

                }
            }
            for(total in totalList){
                if(total.createAt >= initDate.time){
                    allPvrTotalSales += total.sells
                    allPvrtotalGains += (total.money * USER_COMISION)-allPvrTotalOutGoings
                }
            }

            tvOutGoins.text = allPvrTotalOutGoings.toString()
            tvSales.text = allPvrTotalSales.toString()
            tvGains.text = allPvrtotalGains.toString()

        }else if(etInitialDate.text.isNullOrEmpty() && etFinalDate.text.isNotEmpty()){
            for(outgoing in outgoingList){
                if(outgoing.date <= finishDate.time){
                    allPvrTotalOutGoings += outgoing.cost

                }
            }
            for(total in totalList){
                if(total.createAt >= finishDate.time){
                    allPvrTotalSales += total.sells
                    allPvrtotalGains += (total.money * USER_COMISION)-allPvrTotalOutGoings
                }
            }
            tvOutGoins.text = allPvrTotalOutGoings.toString()
            tvSales.text = allPvrTotalSales.toString()
            tvGains.text = allPvrtotalGains.toString()

        }else if(etInitialDate.text.isNullOrEmpty() && etFinalDate.text.isNullOrEmpty()){
            for(outgoing in outgoingList){
                allPvrTotalOutGoings += outgoing.cost
            }
            for (total in totalList) {

                allPvrTotalSales += total.sells
                allPvrtotalGains += (total.money * USER_COMISION) - allPvrTotalOutGoings
            }
            tvOutGoins.text = allPvrTotalOutGoings.toString()
            tvSales.text = allPvrTotalSales.toString()
            tvGains.text = allPvrtotalGains.toString()

        }else {
            for(outgoing in outgoingList){
                if(outgoing.date >=initDate.time && outgoing.date <= finishDate.time){
                    allPvrTotalOutGoings += outgoing.cost

                }
            }
            for(total in totalList){
                if(total.createAt >=initDate.time && total.createAt <= finishDate.time){
                    allPvrTotalSales += total.sells
                    allPvrtotalGains += (total.money * USER_COMISION)-allPvrTotalOutGoings
                }
            }
            tvOutGoins.text = allPvrTotalOutGoings.toString()
            tvSales.text = allPvrTotalSales.toString()
            tvGains.text = allPvrtotalGains.toString()

        }



    }

}