package com.pedrosaez.pvr_control.ui.view.fragments

import android.R.*
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.pedrosaez.pvr_control.database.entities.OutGoins
import com.pedrosaez.pvr_control.database.entities.PvrAndOutGoins
import com.pedrosaez.pvr_control.database.entities.PvrAndTotalRecords
import com.pedrosaez.pvr_control.database.entities.TotalRecords
import com.pedrosaez.pvr_control.databinding.FragmentReportBinding
import com.pedrosaez.pvr_control.ui.dialog.DatePickerFragment
import com.pedrosaez.pvr_control.ui.dialog.twoDigits
import com.pedrosaez.pvr_control.ui.viewmodel.AddPvrViewModel
import com.pedrosaez.pvr_control.ui.viewmodel.OutGoinViewModel
import com.pedrosaez.pvr_control.ui.viewmodel.TotalRecordViewModel
import java.time.ZoneId
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


    @RequiresApi(Build.VERSION_CODES.O)
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
        var pvrList = mutableListOf<String>()

        // creamos el adpatador para el spinner y se lo asignamos
        val spinnerAdapter  = ArrayAdapter(requireContext(), layout.simple_spinner_dropdown_item,pvrList)
        pvrSpinner.adapter = spinnerAdapter
        pvrSpinner.setSelection(0)
        pvrList.add("Todos")
        pvrSpinner.onItemSelectedListener = object:
                AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                _pvrName = pvrSpinner.selectedItem as String
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
              _pvrName = "Todos"
            }

        }




        //observamos los datos de los pvr para actualizar el spinner
        modelPvr.getAll_Pvr.observe(viewLifecycleOwner, {

            pvrList.clear()
            pvrList.add("Totales")

            for (i in it) {
                if (i.user.uid == uid) {
                   userId = i.user.id
                    for (pvr in i.DatosPvr) {
                        val pvrName = pvr.pvrName
                        pvrList.add(pvrName)
                        spinnerAdapter.notifyDataSetChanged()

                    }
                }
            }


        })



        val onePvrTotalList =  mutableListOf<TotalRecords>()
        val onePvrOutGoingList=  mutableListOf<OutGoins>()
        val allPvrTotalList =  mutableListOf<TotalRecords>()
        var allPvrOutGoingList=  mutableListOf<OutGoins>()
        var allPvrAndOutGoingList = mutableListOf<PvrAndOutGoins>()

        var allPvrAndTotalRecords = mutableListOf<PvrAndTotalRecords>()

        modelTotals.getTotalRecords.observe(viewLifecycleOwner, {
            allPvrAndTotalRecords.clear()
            for(i in it){
                if(i.pvr.userId == userId){
                    allPvrAndTotalRecords.add(i)
                }
            }


        })

        modelOutGoing.getAllOutGoins.observe(viewLifecycleOwner, {
            allPvrAndOutGoingList.clear()
            for(i in it) {
                if (i.pvr.userId == userId) {
                    allPvrAndOutGoingList.add(i)
                }
            }
        })

        btSend.setOnClickListener {

            allPvrTotalList.clear()
            allPvrOutGoingList.clear()
            onePvrOutGoingList.clear()
            onePvrTotalList.clear()

          if(_pvrName.equals("Totales")){

              for(i in allPvrAndOutGoingList){
                  for(outgoins in i.outGoins) {
                      allPvrOutGoingList.add(outgoins)
                  }
              }
              for(i in allPvrAndTotalRecords){
                  for(total in i.totalRecords) {
                      allPvrTotalList.add(total)
                  }
              }
              if(allPvrTotalList.isNotEmpty() ) {
                  checkReport(allPvrTotalList, allPvrOutGoingList,
                          tvSales, tvOutGoins, tvGains, reportInitCalendar,
                          reportFinishCalendar, etInitialDate, etFinalDate)
              }else{
                  Snackbar.make(requireView(),"No hay datos que mostrar",Snackbar.LENGTH_SHORT).show()

              }
          }else{
              for(i in allPvrAndOutGoingList){
                  if(_pvrName == i.pvr.pvrName ){
                      for(outgoing in i.outGoins) {
                          onePvrOutGoingList.add(outgoing)
                      }

                  }
              }
              for(i in allPvrAndTotalRecords){
                  if(_pvrName == i.pvr.pvrName ){
                      for(total in i.totalRecords) {
                          onePvrTotalList.add(total)
                      }

                  }
              }
              if (onePvrTotalList.isNotEmpty()) {
                  checkReport(onePvrTotalList, onePvrOutGoingList,
                          tvSales, tvOutGoins, tvGains, reportInitCalendar,
                          reportFinishCalendar, etInitialDate, etFinalDate)
              } else {
                  Snackbar.make(requireView(), "No hay datos que mostrar", Snackbar.LENGTH_SHORT).show()
              }
          }

            etInitialDate.text.clear()
            etFinalDate.text.clear()
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


    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkReport(totalList:MutableList<TotalRecords>, outgoingList:MutableList<OutGoins>,
                            tvSales:TextView, tvOutGoins:TextView,
                            tvGains:TextView, initDate:Calendar, finishDate:Calendar, etInitialDate:EditText, etFinalDate:EditText){

        //tranformamos las fechas a localDate para eliminar la hora, minutos y segundos
        val initDateLocal = initDate.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val finishDateLocal = finishDate.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        var sales = 0L
        var gains = 0.0
        var salesTotal = 0L
        var outgoings = 0.0


        // variable paa obtener una lista de totales filtrada por fechas
        val totalListFilter = mutableListOf<TotalRecords>()

        if(etInitialDate.text.isNotEmpty() && etFinalDate.text.isNullOrEmpty()){// campo fecha inicial con datos y campo final vacio, obtenemos los totales
            // desde  fecha hasta la actualidad


            // recorremos la lista de gasto  y sumamos los gastos que corresponden a las fechas filtradas
            for(outgoing in outgoingList){
                val outGoingDate = outgoing.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                if(outGoingDate >= initDateLocal){
                    outgoings += outgoing.cost
                }
            }

            //recorremos la lista de totales y obtenemos una lsita filtrada por fechas
            for(total in totalList){
                val totalDate = total.createAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                if(totalDate >= initDateLocal){
                    totalListFilter.add(total)
                }
            }
            // obtenemos las ventas restando a  la cantidad del ultimo registro de totales de la lista filtrada la cantidad del primer registro de totales
            sales = totalListFilter.last().sells-totalList.first().sells
            //restamos el primer registro de dinero de la lista totales al  ultimo registro de dienro de la lista filtrada
            // lo multiplicamos por la comision que se lleva de las ventas el usuario y restamos los gastos correspondientes al rango de fechas
            gains += ((totalListFilter.last().money - totalList.first().money) * USER_COMISION ) -outgoings

            fillTextview(tvOutGoins,tvSales,tvGains,outgoings,sales,gains)


        }else if(etInitialDate.text.isNullOrEmpty() && etFinalDate.text.isNotEmpty()){ // campo fecha inicial vacio y campo final con datos, obtenemos los totales
            // desde  la creacion hasta la fecha indicada


            // recorremos la lista de gasto  y sumamos los gastos que corresponden a las fechas filtradas
            for(outgoing in outgoingList){
                val outGoingDate = outgoing.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                if(outGoingDate <= finishDateLocal){
                    outgoings += outgoing.cost

                }
            }
            //recorremos la lista de totales y obtenemos una lsita filtrada por fechas
            for(total in totalList){
                val totalDate = total.createAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                if(totalDate >= finishDateLocal){
                    totalListFilter.add(total)
                }
            }
            // obtenemos las ventas restando a  la cantidad del ultimo registro de totales de la lista filtrada la cantidad del primer registro de totales
            sales = totalListFilter.last().sells-totalList.first().sells

            //restamos el primer registro de dinero de la lista totales al  ultimo registro de dienro de la lista filtrada
            // lo multiplicamos por la comision que se lleva de las ventas el usuario y restamos los gastos correspondientes al rango de fechas
            gains += ((totalListFilter.last().money - totalList.first().money) * USER_COMISION ) -outgoings
            fillTextview(tvOutGoins,tvSales,tvGains,outgoings,sales,gains)


        }else if(etInitialDate.text.isNullOrEmpty() && etFinalDate.text.isNullOrEmpty()){ // los campos estan vacios por lo que se obtendran los totales de todas las fechas

            // recorremos la lista de gasto  y sumamos los gastos que corresponden a las fechas filtradas
            for(outgoing in outgoingList){
                outgoings += outgoing.cost
            }

            // obtenemos las ventas restando a la cantidad del ultimo registro de totales  la cantidad del primer registro de totales
            sales = totalList.last().sells-totalList.first().sells

            //restamos el primer registro de dinero de la lista totales al  ultimo registro de dinero de  la misma lista
            // lo multiplicamos por la comision que se lleva de las ventas el usuario y restamos los gastos totales
            gains += ((totalList.last().money - totalList.first().money) * USER_COMISION ) -outgoings
            fillTextview(tvOutGoins,tvSales,tvGains,outgoings,sales,gains)


        }else {
            // si los dos campos de fechas estan con datos,obtenemos los totales entre las fechas introducidas

            // recorremos la lista de gasto  y sumamos los gastos que corresponden a las fechas filtradas
            for(outgoing in outgoingList){
                val outGoingDate = outgoing.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                if(outGoingDate in initDateLocal..finishDateLocal){
                    outgoings += outgoing.cost

                }
            }

            //recorremos la lista de totales y obtenemos una lsita filtrada por fechas
            for(total in totalList){
                val totalDate = total.createAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                if(totalDate in initDateLocal..finishDateLocal){
                    totalListFilter.add(total)
                }
            }
            // obtenemos las ventas restando a  la cantidad del ultimo registro de totales de la lista filtrada la cantidad del primer registro de totales
            sales = totalListFilter.last().sells-totalList.first().sells
            //restamos el primer registro de dinero de la lista totales al  ultimo registro de dinero de  la misma lista
            // lo multiplicamos por la comision que se lleva de las ventas el usuario y restamos los gastos totales
            gains += ((totalListFilter.last().money - totalList.first().money) * USER_COMISION ) -outgoings

            fillTextview(tvOutGoins,tvSales,tvGains,outgoings,sales,gains)


        }



    }

    //funcion que llena los textview con los datos obtenidos
    fun fillTextview(tvOutGoins:TextView,tvSales:TextView,tvGains:TextView,outgoings:Double,sales:Long,gains:Double){

        tvOutGoins.text = outgoings.toString() + " \u20ac"
        tvSales.text = sales.toString() +" paquetes"
        tvGains.text =  String.format("%.2f", gains)  + " \u20ac"

    }


}