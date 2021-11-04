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
import androidx.lifecycle.MediatorLiveData
import com.google.android.material.snackbar.Snackbar
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.ParcialRecords
import com.pedrosaez.pvr_control.database.entities.PvrAndParcialRecords
import com.pedrosaez.pvr_control.database.entities.PvrAndTotalRecords
import com.pedrosaez.pvr_control.database.entities.TotalRecords
import com.pedrosaez.pvr_control.databinding.RecordsDialogBinding
import com.pedrosaez.pvr_control.ui.viewmodel.ParcialRecordViewModel
import com.pedrosaez.pvr_control.ui.viewmodel.TotalRecordViewModel


// Dialogo para introducir los registros de las máquinas de los pvr
class AddRecordDialog: DialogFragment() {

    private var _binding: RecordsDialogBinding?= null
    private val binding get() = _binding!!
    private lateinit var  totalRecords : TotalRecords
    private lateinit var  parcialRecords : ParcialRecords
    private var _pvrId: Long? = null

    private val modelTotal: TotalRecordViewModel by activityViewModels()
    private val modelParcial: ParcialRecordViewModel by activityViewModels()



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
            val totalMoney = binding.etTotalMoney


            // obtenemos el Id del pvr
            val prefs = requireActivity().getSharedPreferences((getString(R.string.prefs_file)), Context.MODE_PRIVATE)
            _pvrId = prefs.getLong("pvrId", -1)


            builder.setPositiveButton(
                    if (tag == "addRecord") {
                        "Guardar"
                    } else "Actualizar",
                    DialogInterface.OnClickListener { dialog, id ->

                        var totalSalesLong = 0L
                        var totalBillsLong = 0L
                        var totalCoinsLong = 0L
                        var totalMoneyLong = 0L
                        if (!totalSales.text.isNullOrEmpty()) {
                            totalSalesLong = totalSales.text.toString().toLong()
                        }
                        if (!totalBills.text.isNullOrEmpty()) {
                            totalBillsLong = totalBills.text.toString().toLong()
                        }
                        if (!totalCoins.text.isNullOrEmpty()) {
                            totalCoinsLong = totalCoins.text.toString().toLong()
                        }
                        if (!totalMoney.text.isNullOrEmpty()) {
                            totalMoneyLong = totalCoins.text.toString().toLong()
                        }



                        totalRecords = TotalRecords(totalSalesLong, totalBillsLong, totalCoinsLong,totalMoneyLong, _pvrId!!)
                        parcialRecords = ParcialRecords(totalSalesLong, totalBillsLong, totalCoinsLong,totalMoneyLong, _pvrId!!)


                        if (tag == "addRecord") {
                            if (totalSales.text.toString().isNotEmpty() && totalBills.text.toString().isNotEmpty()
                                    && totalCoins.text.toString().isNotEmpty() && totalMoney.text.toString().isNotEmpty()) {
                                modelTotal.saveTotal(totalRecords)
                                modelParcial.saveParcial(parcialRecords)
                            } else {
                                Snackbar.make(viewParentfragment!!, getString(R.string.must_fill_fields), Snackbar.LENGTH_LONG)
                                        .setAnchorView(R.id.bt_new_record)
                                        .show()

                            }
                        } else { // en el caso de que el tag sea updateRecord

                            // comprobamos si hay datos introducidos
                            if (!checkAndUpdateTotals(totalRecords)) {
                                Snackbar.make(viewParentfragment!!, getString(R.string.no_data_found_to_update), Snackbar.LENGTH_LONG)
                                        .setAnchorView(R.id.bt_new_record)//mostramos en snackbar encima del floating button
                                        .show()
                            }
                            if(!checkAndUpdateParcial(parcialRecords) ){
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

    // función que comprueba si hay algun dato introducido en el dialogo y actualiza el registro de los totales
    fun checkAndUpdateTotals (updatedRecord :TotalRecords):Boolean{

        val actualTotalRecord:TotalRecords?=getTotals()

        // variable que checkea si se a introducido alguna cantidad
        var updateSomeField = false

        if(updatedRecord.sells > 0){
            actualTotalRecord!!.sells = updatedRecord.sells
            updateSomeField = true
        }
        if(updatedRecord.bills > 0){
            actualTotalRecord!!.bills = updatedRecord.bills
            updateSomeField = true
        }
        if(updatedRecord.coins > 0 ){
            actualTotalRecord!!.coins = updatedRecord.coins
            updateSomeField = true
        }

        // si hay algo introducido en cualquiera de los campos actualizamos parcial y total
        if (updateSomeField) {

            modelTotal.updateTotal(actualTotalRecord!!)
        }

        return updateSomeField

    }

    // funcion que comprueba si hay algun dato introducido en el dialogo y actualiza el registro de los parciales
    fun checkAndUpdateParcial (updatedRecord :ParcialRecords):Boolean{

        val actualParcialRecord:ParcialRecords?=getParcial()


        // variable que checkea si se a introducido alguna cantidad
        var updateSomeField = false

        if(updatedRecord.sells > 0){
            actualParcialRecord!!.sells = updatedRecord.sells
            updateSomeField = true
        }
        if(updatedRecord.bills > 0){
            actualParcialRecord!!.bills = updatedRecord.bills
            updateSomeField = true
        }
        if(updatedRecord.coins > 0 ){
            actualParcialRecord!!.coins = updatedRecord.coins
            updateSomeField = true
        }

        // si hay algo introducido en cualquiera de los campos actualizamos parcial y total
        if (updateSomeField) {
            modelParcial.updateParcial(actualParcialRecord!!)
        }

        return updateSomeField

    }

    // obtenemos los registros de pvr y totales y los devolvemos
    fun getTotals(): TotalRecords? {
        val recordsAndPvrTotal: List<PvrAndTotalRecords>? = modelTotal.getTotalRecords.value
        var actualTotalRecord:TotalRecords?=null

        if (recordsAndPvrTotal != null) {
            for (i in recordsAndPvrTotal) {
                if (i.pvr.id == _pvrId) {
                    actualTotalRecord = i.totalRecords.last()
                }

            }
        }
        return actualTotalRecord
    }
    // obtenemos los registros de pvr y parciales y los devolvemos
    fun getParcial(): ParcialRecords? {
        val recordsAndPvrParcial: List<PvrAndParcialRecords>? = modelParcial.getParcialRecords.value
        var actualParcialRecord:ParcialRecords?=null

        if (recordsAndPvrParcial != null) {
            for (i in recordsAndPvrParcial) {
                if (i.pvr.id == _pvrId) {
                    actualParcialRecord = i.parcialRecords.last()
                }

            }
        }
        return actualParcialRecord
    }
}