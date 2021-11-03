package com.pedrosaez.pvr_control.ui.view.fragments

import android.content.Context
import android.icu.text.AlphabeticIndex
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.Records
import com.pedrosaez.pvr_control.databinding.FragmentSalesSummaryBinding
import com.pedrosaez.pvr_control.ui.viewmodel.RecordViewModel
import kotlin.math.absoluteValue

class SalesSummaryFragment : Fragment() {



    private var lastRecord: Records? = null
    private var parcialFirstRecord : Records? = null
    private var totalFirstRecord : Records? = null
    private var _binding: FragmentSalesSummaryBinding? = null
    private val binding get() = _binding!!
    private val model: RecordViewModel by activityViewModels()

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
        val parcialGain = binding.tvParcialGains
        val btRestartParcial = binding.btRestartParcials



        var parcialSalesInt: Int
        var parcialBillInt = 0
        var parcialCoinsInt = 0
        var parcialPaymentDouble: Double
        var totalGainInt :Double
        var totalPaymentPvrInt:Int
        



        model.getParcialRecords.observe(viewLifecycleOwner, { recordList ->


            for (i in recordList) {
                if (recordList != null) {
                    if (i.pvr.id == pvrId) {
                        if (i.records.isNotEmpty()) {
                            lastRecord = i.records.last()
                            parcialFirstRecord = i.records.first()
                            if (i.records.size > 1) {
                                parcialSalesInt = lastRecord!!.sells - parcialFirstRecord!!.sells
                                parcialBillInt = lastRecord!!.bills - parcialFirstRecord!!.bills
                                parcialCoinsInt = lastRecord!!.coins - parcialFirstRecord!!.coins
                            } else {
                                parcialSalesInt = 0
                                parcialBillInt = 0
                                parcialCoinsInt = 0
                            }
                            parcialPaymentDouble = parcialSalesInt * 0.15
                            parcialSales.text = (parcialSalesInt).toString()
                            parcialBills.text = (parcialBillInt).toString()
                            parcialCoins.text = (parcialCoinsInt).toString()
                            parcialPaymentsPvr.text = parcialPaymentDouble.toString()

                        } else {
                            parcialSales.text = ""
                            parcialBills.text = ""
                            parcialCoins.text = ""
                        }

                    }
                } else {


                }
            }

            btRestartParcial.setOnClickListener {
                for (i in recordList) {
                    if (recordList != null) {
                        if (i.pvr.id == pvrId) {
                            for (x in i.records) {
                            }
                        }
                    }
                }

            }

        })




        return binding.root
    }

}