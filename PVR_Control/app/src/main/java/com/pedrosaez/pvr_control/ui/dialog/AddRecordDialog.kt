package com.pedrosaez.pvr_control.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.PvrAndRecords
import com.pedrosaez.pvr_control.database.entities.PvrMachine
import com.pedrosaez.pvr_control.database.entities.Records
import com.pedrosaez.pvr_control.databinding.MachineDialogBinding
import com.pedrosaez.pvr_control.databinding.RecordsDialogBinding
import com.pedrosaez.pvr_control.ui.viewmodel.RecordViewModel

class AddRecordDialog: DialogFragment() {

    private var _binding: RecordsDialogBinding?= null
    private val binding get() = _binding!!
    private lateinit var  records : Records
    private var _pvrId: Long? = null

    private val model: RecordViewModel by activityViewModels()



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            _binding = RecordsDialogBinding.inflate(LayoutInflater.from(it))
            val builder = AlertDialog.Builder(it)

            builder.setView(binding.root)

            //setup
            val viewParentfragment: View? = parentFragment?.requireView()
            val totalSales = binding.etTotalSales
            val totalBills = binding.etTotalBills
            val totalCoins = binding.etTotalCoins

            // obtenemos el Id del pvr
            val prefs = requireActivity().getSharedPreferences((getString(R.string.prefs_file)), Context.MODE_PRIVATE)
            _pvrId = prefs.getLong("pvrId", -1)


            builder.setPositiveButton(
                    if (tag == "addRecord") {
                        "Guardar"
                    } else "Actualizar",
                    DialogInterface.OnClickListener { dialog, id ->

                        var totalSalesInt = 0
                        var totalBillsInt = 0
                        var totalCoinsInt = 0
                        if (!totalSales.text.isNullOrEmpty()) {
                            totalSalesInt = totalSales.text.toString().toInt()
                        }
                        if (!totalBills.text.isNullOrEmpty()) {
                            totalBillsInt = totalBills.text.toString().toInt()
                        }
                        if (!totalCoins.text.isNullOrEmpty()) {
                            totalCoinsInt = totalCoins.text.toString().toInt()
                        }


                        records = Records(totalSalesInt, totalBillsInt, totalCoinsInt, _pvrId!!)

                        if (tag == "addRecord") {
                            if (totalSales.text.toString().isNotEmpty() && totalBills.text.toString().isNotEmpty() && totalCoins.text.toString().isNotEmpty()) {
                                model.save(records)
                            } else {
                                Snackbar.make(viewParentfragment!!, getString(R.string.must_fill_fields), Snackbar.LENGTH_LONG)
                                        .setAnchorView(R.id.bt_new_record)
                                        .show()

                            }
                        } else {

                            if (!checkAndUpdateData(records)) {
                                Snackbar.make(viewParentfragment!!, getString(R.string.no_data_found_to_update), Snackbar.LENGTH_LONG)
                                        .setAnchorView(R.id.bt_new_record)//mostramos en snackbar encima del floating button
                                        .show()
                            }
                        }


                    })
            builder.setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun checkAndUpdateData (updatedRecord :Records):Boolean{

        val recordsAndPvr: List<PvrAndRecords>? = model.getTotalRecords.value
        var actualRecord:Records?=null

        if (recordsAndPvr != null) {
            for (i in recordsAndPvr) {
                if (i.pvr.id == _pvrId) {
                    actualRecord = i.records.last()
                }

            }
        }
        var updateSomeField = false

        if(updatedRecord.sells > 0){
            actualRecord!!.sells = updatedRecord.sells
            updateSomeField = true
        }
        if(updatedRecord.bills > 0){
            actualRecord!!.bills = updatedRecord.bills
            updateSomeField = true
        }
        if(updatedRecord.coins > 0 ){
            actualRecord!!.coins = updatedRecord.coins
            updateSomeField = true
        }

        if (updateSomeField) {
            model.update(actualRecord!!)
        }

        return updateSomeField

    }

}