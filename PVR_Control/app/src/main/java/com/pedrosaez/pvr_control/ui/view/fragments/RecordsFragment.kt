package com.pedrosaez.pvr_control.ui.view.fragments

import android.content.Context
import android.icu.text.AlphabeticIndex
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.ParcialRecords
import com.pedrosaez.pvr_control.database.entities.TotalRecords
import com.pedrosaez.pvr_control.databinding.FragmentRecordsBinding
import com.pedrosaez.pvr_control.ui.dialog.AddRecordDialog
import com.pedrosaez.pvr_control.ui.viewmodel.ParcialRecordViewModel
import com.pedrosaez.pvr_control.ui.viewmodel.TotalRecordViewModel
import kotlin.properties.Delegates


class RecordsFragment : Fragment() {


    private var lastRecord: TotalRecords? = null
    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!
    var cardIsvisible:Boolean = false
    var helpIsvisible:Boolean = true

    private val modelTotal: TotalRecordViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordsBinding.inflate(layoutInflater)

        //obtenemos los datos del Pvr que vamos a usar
        val prefs = requireActivity().getSharedPreferences((getString(R.string.prefs_file)), Context.MODE_PRIVATE)
        val pvrId = prefs.getLong("pvrId", -1)


        //setup
        val addRecordDialog = AddRecordDialog()
        val sales = binding.tvSales
        val bills = binding.tvBills
        val coins = binding.tvCoins
        val money = binding.tvMoney
        val btEdit= binding.btEdit
        val btAddRecord = binding.btNewRecord
        val cardView = binding.cardView
        val helpText = binding.help

        helpText.isVisible= helpIsvisible
        cardView.isVisible = cardIsvisible


        modelTotal.getTotalRecords.observe(viewLifecycleOwner, {

            /*obtenemos la lista de registros de el pvr por Id , y llenamos los textview con los datos
             del último registro*/

            for (i in it) {
                if (it != null) {
                    if (i.pvr.id == pvrId) {
                        if (i.totalRecords.isNotEmpty()) {
                            helpIsvisible= false
                            cardIsvisible = true
                            cardView.isVisible = cardIsvisible
                            helpText.isVisible = helpIsvisible
                            lastRecord = i.totalRecords.last()
                            sales.text = lastRecord!!.sells.toString() +"  Paquetes"
                            bills.text = lastRecord!!.bills.toString() + " \u20ac"
                            coins.text = lastRecord!!.coins.toString() + " \u20ac"
                            money.text = lastRecord!!.money.toString() + " \u20ac"
                        }
                    }
                }
            }
        })

        btAddRecord.setOnClickListener {
            addRecordDialog.show(childFragmentManager,"addRecord")
        }



        btEdit.setOnClickListener {
            addRecordDialog.show(childFragmentManager,"updateRecord")

        }


        return binding.root
    }

}