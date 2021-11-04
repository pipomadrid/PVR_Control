package com.pedrosaez.pvr_control.ui.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.TotalRecords
import com.pedrosaez.pvr_control.databinding.FragmentRecordsBinding
import com.pedrosaez.pvr_control.ui.dialog.AddRecordDialog
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


        //setup
        //obtenemos los datos del Pvr que vamos a usar
        val prefs = requireActivity().getSharedPreferences((getString(R.string.prefs_file)), Context.MODE_PRIVATE)
        val pvrNamePrefs: String? = prefs.getString("pvrName", "error ")
        val pvrId = prefs.getLong("pvrId", -1)

        val addRecordDialog = AddRecordDialog()
        val sales = binding.tvSales
        val bills = binding.tvBills
        val coins = binding.tvCoins
        val money = binding.tvMoney
        val btEdit= binding.btEdit
        val btDelete = binding.btDelete
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
                            sales.text = lastRecord!!.sells.toString()
                            bills.text = lastRecord!!.bills.toString()
                            coins.text = lastRecord!!.coins.toString()
                            money.text = lastRecord!!.money.toString()
                        }
                    }
                }
            }
        })

        btAddRecord.setOnClickListener {
            addRecordDialog.show(childFragmentManager,"addRecord")
        }

       /* btDelete.setOnClickListener {
            view
            val builder = AlertDialog.Builder(context)
            builder.setMessage("¿Estas seguro de eliminar los datos del ultimo registro?, se modificarán los datos de venta del pvr.")
                    //al confirmar , se elimina el registro de la maquina
                    .setPositiveButton("Eliminar",
                            DialogInterface.OnClickListener { dialog, id ->
                                model.delete(lastRecord!!)
                                cardView.isVisible = false

                            })
                    .setNegativeButton(("cancelar"),
                            DialogInterface.OnClickListener { dialog, id ->
                                dialog.cancel()
                            })
            // Create the AlertDialog object and return it
            builder.create()
            builder.show()

        }*/

        btEdit.setOnClickListener {

            addRecordDialog.show(childFragmentManager,"updateRecord")


        }


        return binding.root
    }
    private fun deleteRecord(record: TotalRecords) {
        binding.cardView.isVisible = false
    }



}