package com.pedrosaez.pvr_control.ui.view.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.OutGoins
import com.pedrosaez.pvr_control.database.entities.ParcialRecords
import com.pedrosaez.pvr_control.database.entities.TotalRecords
import com.pedrosaez.pvr_control.databinding.FragmentSalesSummaryBinding
import com.pedrosaez.pvr_control.ui.viewmodel.OutGoinViewModel
import com.pedrosaez.pvr_control.ui.viewmodel.ParcialRecordViewModel
import com.pedrosaez.pvr_control.ui.viewmodel.TotalRecordViewModel
import java.text.DecimalFormat

const val PVR_COMISION = 0.15
const val USER_COMISION =0.085

class SalesSummaryFragment : Fragment() {



    private var parcialLastRecord: ParcialRecords? = null
    private var parcialFirstRecord : ParcialRecords? = null
    private var totalLastRecord: TotalRecords? = null
    private var totalFirstRecord : TotalRecords? = null
    private var _binding: FragmentSalesSummaryBinding? = null
    private val binding get() = _binding!!
    private val modelParcial: ParcialRecordViewModel by activityViewModels()
    private val modelTotal: TotalRecordViewModel by activityViewModels()
    private val modelOutGoing: OutGoinViewModel by activityViewModels()
    private var outGoingList: List<OutGoins>? =null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentSalesSummaryBinding.inflate(layoutInflater)



        //obtenemos los datos del pvr
        val prefs = requireActivity().getSharedPreferences((getString(R.string.prefs_file)), Context.MODE_PRIVATE)
        val pvrNamePrefs: String? = prefs.getString("pvrName", "error ")
        val pvrId = prefs.getLong("pvrId", -1)



        //Setup
        //Parcials textview
        val parcialSales = binding.tvParcialSells
        val parcialBills = binding.tvParcialBills
        val parcialCoins = binding.tvParcialCoins
        val parcialPaymentsPvr = binding.tvPvrPayment
        val parcialGain = binding.tvParcialGains
        val parcialOutgoins = binding.tvParcialOutgoins

        // Restart parcial buttons
        val btRestartParcial = binding.btRestartParcials
        val btRestartBills = binding.restartBills
        val btRestartCoins = binding.restartCoins


        // Total textViews
        val totalPaymentPvr = binding.tvTotalPaymentPvr
        val totalGain = binding.tvTotalGains
        var totalOutGoins = binding.tvTotalOutgoins




        // variables para realizar las operaciones
        var parcialSalesLong: Long
        var parcialBillLong = 0L
        var parcialCoinsDouble = 0.0
        var parcialMoneyDouble = 0.0
        var parcialPaymentDouble: Double
        var parcialGainDouble:Double = 0.0
        var totalGainDouble :Double = 0.0
        var totalPaymentPvrDouble:Double = 0.0
    /*    var outGoingListWithParcials: MutableList<OutGoins>? = null*/
        var parcialOutGoinsAmount:Double =0.0
        var totalOutGoinsAmount:Double =0.0
         var  totalGainDoubleMinusOutGoings = 0.0



        modelOutGoing.getAllOutGoins.observe(viewLifecycleOwner,{pvrAndOutGoins->
            totalOutGoinsAmount =0.0
            for(pvrAndOut in pvrAndOutGoins){
                if(pvrAndOut.pvr.id == pvrId){
                    if(pvrAndOut.outGoins.isNotEmpty()) {
                        outGoingList = pvrAndOut.outGoins
                        for(x in outGoingList!!) {

                            totalOutGoinsAmount += x.cost
                        }
                    }else{
                        totalOutGoinsAmount = 0.0
                    }
                }

            }

            totalGainDoubleMinusOutGoings =totalGainDouble - totalOutGoinsAmount
            totalOutGoins.text = totalOutGoinsAmount.toString() + " \u20ac"
            parcialOutgoins.text = parcialOutGoinsAmount.toString() +" \u20ac"
            totalGain.text = String.format("%.2f", totalGainDoubleMinusOutGoings) +" \u20ac"
           /* outGoingList = it*/
           /* if(!outGoingList.isNullOrEmpty()) {
                for (outgoing in outGoingList!!) {
                    //obtenemmos los totales de los gastos
                    totalOutGoinsAmount += outgoing.cost
                }
            }else {
                totalOutGoinsAmount = 0.0
            }*/


        })

        modelParcial.getParcialRecords.observe(viewLifecycleOwner, { recordList ->

            parcialOutGoinsAmount = 0.0

            for (record in recordList) {
                if (recordList != null) {
                    // obtenemos la lista de parciales con el id del pvr
                    if (record.pvr.id == pvrId) {
                        //comprobamos que la lista tenga datos,si los tienes realizamos operaciones
                        if (record.parcialRecords.isNotEmpty()) {
                            parcialLastRecord = record.parcialRecords.last()
                            parcialFirstRecord = record.parcialRecords.first()
                            val timeLast = parcialLastRecord!!.createAt.time
                            val timeFirst = parcialFirstRecord!!.createAt.time
                            //Iteramos sobre  los gastos  entre las fechas de los parciales y  obtenemos la suma
                            if (outGoingList?.isNotEmpty() == true) {
                                for (outgoing in outGoingList!!) {
                                    val outgoinDate = outgoing.date.time
                                    if (outgoinDate in timeFirst..timeLast) {
                                        parcialOutGoinsAmount += outgoing.cost
                                    }
                                }
                            }
                            // si la lista es mayor a 1 realizamos los cálculos entre los registros
                            // ultimo y primero
                            if (record.parcialRecords.size > 1) {
                                parcialSalesLong = parcialLastRecord!!.sells - parcialFirstRecord!!.sells
                                parcialBillLong = parcialLastRecord!!.bills - parcialFirstRecord!!.bills
                                parcialCoinsDouble = parcialLastRecord!!.coins - parcialFirstRecord!!.coins
                                parcialMoneyDouble = parcialLastRecord!!.money - parcialFirstRecord!!.money
                            } else {
                                //si la lista solo tiene un registro
                                parcialSalesLong = 0
                                parcialBillLong = 0
                                parcialCoinsDouble = 0.0
                                parcialMoneyDouble = 0.0
                                parcialOutGoinsAmount = 0.0
                            }
                            //Introducomos los datos en los textView para mostrarlos
                            parcialPaymentDouble = parcialSalesLong * PVR_COMISION
                            parcialGainDouble = (parcialMoneyDouble * USER_COMISION) - parcialOutGoinsAmount
                            parcialSales.text = (parcialSalesLong).toString()  + " paquetes"
                            parcialBills.text = (parcialBillLong).toString() + " \u20ac"
                            parcialCoins.text = (parcialCoinsDouble).toString() + " \u20ac"
                            parcialPaymentsPvr.text = String.format("%.2f", parcialPaymentDouble) + " \u20ac"
                            parcialGain.text = String.format("%.2f", parcialGainDouble) + " \u20ac"
                            parcialOutgoins.text = String.format("%.2f", parcialOutGoinsAmount) + " \u20ac"


                        } else {
                            parcialSales.text = ""
                            parcialBills.text = ""
                            parcialCoins.text = ""
                        }
                    }
                }
            }

            btRestartParcial.setOnClickListener {

                val builder = AlertDialog.Builder(context)
                builder.setMessage("¿Seguro que quieres poner a 0 los parciales?")
                        .setPositiveButton("resetear",
                                DialogInterface.OnClickListener { dialog, id ->
                                    for (i in recordList) {
                                        if (recordList != null) {
                                            // obtenemos la lista de parciales con el id del pvr
                                            if (i.pvr.id == pvrId) {
                                                // por cada registro  del pvr borramos todos menos el último
                                                for (x in i.parcialRecords) {
                                                    if (x != i.parcialRecords.last()) {
                                                        modelParcial.deleteParcial(x)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    parcialSales.text = ""
                                    parcialBills.text = ""
                                    parcialCoins.text = ""
                                })
                        .setNegativeButton(("cancelar"),
                                DialogInterface.OnClickListener { dialog, id ->
                                    dialog.cancel()
                                })
                // Create the AlertDialog object and return it
                builder.create()
                builder.show()

            }


            btRestartBills.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("¿Seguro que quieres poner a 0 el registro de billetes?")
                        .setPositiveButton("resetear",
                                DialogInterface.OnClickListener { dialog, id ->
                                    for(i in recordList){
                                        if (recordList != null) {
                                            // obtenemos la lista de parciales con el id del pvr
                                            if (i.pvr.id == pvrId) {
                                                //  ponemos a 0  los billetes de los parciales del pvr
                                                for(x in i.parcialRecords){
                                                    x.bills = 0
                                                    modelParcial.updateParcial(x)
                                                }
                                            }
                                        }
                                    }
                                })
                        .setNegativeButton(("cancelar"),
                                DialogInterface.OnClickListener { dialog, id ->
                                    dialog.cancel()
                                })
                // Create the AlertDialog object and return it
                builder.create()
                builder.show()

            }



            btRestartCoins.setOnClickListener {

                val builder = AlertDialog.Builder(context)
                builder.setMessage("¿Seguro que quieres poner a 0 el registro de monedas?")
                        .setPositiveButton("resetear",
                                DialogInterface.OnClickListener { dialog, id ->
                                    for(i in recordList){
                                        if (recordList != null) {
                                            // obtenemos la lista de parciales con el id del pvr
                                            if (i.pvr.id == pvrId) {
                                                //  ponemos a 0  los billetes de los parciales del pvr
                                                for(x in i.parcialRecords){
                                                    x.coins = 0.0
                                                    modelParcial.updateParcial(x)
                                                }
                                            }                        }
                                    }
                                })
                        .setNegativeButton(("cancelar"),
                                DialogInterface.OnClickListener { dialog, id ->
                                    dialog.cancel()
                                })
                // Create the AlertDialog object and return it
                builder.create()
                builder.show()

            }


        })

        modelTotal.getTotalRecords.observe(viewLifecycleOwner, { recordList ->

            for (i in recordList) {
                if (recordList != null) {
                    // obtenemos la lista de totales con el id del pvr
                    if (i.pvr.id == pvrId) {
                        //comprobamos que la lista tenga datos,si los tienes realizamos operaciones
                        if (i.totalRecords.isNotEmpty()) {
                            totalLastRecord = i.totalRecords.last()
                            totalFirstRecord = i.totalRecords.first()
                            totalGainDouble = ((totalLastRecord!!.money - totalFirstRecord!!.money) * USER_COMISION) - totalOutGoinsAmount
                            totalPaymentPvrDouble = (totalLastRecord!!.sells - totalFirstRecord!!.sells) * PVR_COMISION

                            totalPaymentPvr.text = String.format("%.2f",totalPaymentPvrDouble) + " \u20ac"
                            totalGain.text= String.format("%.2f",totalGainDouble) + " \u20ac"

                        }
                    }
                }
            }



        })





        return binding.root
    }



}