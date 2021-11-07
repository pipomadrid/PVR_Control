package com.pedrosaez.pvr_control.ui.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.ParcialRecords
import com.pedrosaez.pvr_control.database.entities.TotalRecords
import com.pedrosaez.pvr_control.databinding.FragmentSalesSummaryBinding
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentSalesSummaryBinding.inflate(layoutInflater)



        //setup
        val prefs = requireActivity().getSharedPreferences((getString(R.string.prefs_file)), Context.MODE_PRIVATE)
        val pvrNamePrefs: String? = prefs.getString("pvrName", "error ")
        val pvrId = prefs.getLong("pvrId", -1)

        val parcialSales = binding.tvParcialSells
        val parcialBills = binding.tvParcialBills
        val parcialCoins = binding.tvParcialCoins
        val parcialOutgoins = binding.tvParcialOutgoins
        val parcialPaymentsPvr = binding.tvPvrPayment
        val totalPaymentPvr = binding.tvTotalPaymentPvr
        val totalGain = binding.tvTotalGains
        val parcialGain = binding.tvParcialGains
        val btRestartParcial = binding.btRestartParcials



        var parcialSalesLong: Long
        var parcialBillLong = 0L
        var parcialCoinsLong = 0L
        var parcialMoneyLong = 0L
        var parcialPaymentDouble: Double
        var parcialGainDouble:Double
        var totalGainDouble :Double
        var totalPaymentPvrInt:Long




        modelParcial.getParcialRecords.observe(viewLifecycleOwner, { recordList ->


            for (i in recordList) {
                if (recordList != null) {
                    // obtenemos la lista de parciales con el id del pvr
                    if (i.pvr.id == pvrId) {
                        //comprobamos que la lista tenga datos,si los tienes realizamos operaciones
                        if (i.parcialRecords.isNotEmpty()) {
                            parcialLastRecord = i.parcialRecords.last()
                            parcialFirstRecord = i.parcialRecords.first()
                            // si la lista es mayor a 1 realizamos los cálculos entre los registros
                            // ultimo y primero
                            if (i.parcialRecords.size > 1) {
                                parcialSalesLong = parcialLastRecord!!.sells - parcialFirstRecord!!.sells
                                parcialBillLong = parcialLastRecord!!.bills - parcialFirstRecord!!.bills
                                parcialCoinsLong = parcialLastRecord!!.coins - parcialFirstRecord!!.coins
                                parcialMoneyLong = parcialLastRecord!!.money - parcialFirstRecord!!.money
                            } else {
                                //si la lista solo tiene un registro
                                parcialSalesLong = 0
                                parcialBillLong = 0
                                parcialCoinsLong = 0
                                parcialMoneyLong = 0
                            }
                            parcialPaymentDouble = parcialSalesLong * PVR_COMISION
                            parcialGainDouble =  parcialMoneyLong * USER_COMISION
                            parcialSales.text = (parcialSalesLong).toString()
                            parcialBills.text = (parcialBillLong).toString()
                            parcialCoins.text = (parcialCoinsLong).toString()
                            parcialPaymentsPvr.text = String.format("%.2f",parcialPaymentDouble)
                            parcialGain.text = String.format("%.2f",parcialGainDouble)

                        } else {
                            parcialSales.text = ""
                            parcialBills.text = ""
                            parcialCoins.text = ""
                        }
                    }
                }
            }

            btRestartParcial.setOnClickListener {
                for (i in recordList) {
                    if (recordList != null) {
                        // obtenemos la lista de parciales con el id del pvr
                        if (i.pvr.id == pvrId) {
                            // por cada registro  del pvr borramos todos menos el último
                            for (x in i.parcialRecords) {
                                if(x != i.parcialRecords.last()){
                                    modelParcial.deleteParcial(x)
                                }
                            }
                        }
                    }
                }
                parcialSales.text = ""
                parcialBills.text = ""
                parcialCoins.text = ""


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
                            totalGainDouble = (totalLastRecord!!.money - totalFirstRecord!!.money) * USER_COMISION
                            totalGain.text = String.format("%.2f",totalGainDouble)
                        }
                    }
                }
            }


        })



        return binding.root
    }

}