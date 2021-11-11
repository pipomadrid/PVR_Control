package com.pedrosaez.pvr_control.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedrosaez.pvr_control.application.App
import com.pedrosaez.pvr_control.database.entities.PvrAndTotalRecords
import com.pedrosaez.pvr_control.database.entities.TotalRecords
import com.pedrosaez.pvr_control.repository.TotalRecordsRepository
import com.pedrosaez.pvr_control.ui.view.fragments.USER_COMISION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId

class TotalRecordViewModel:ViewModel(){

    private val repositoryTotals : TotalRecordsRepository
    val getTotalRecords : LiveData<List<PvrAndTotalRecords>>


    init{
        val db=App.obtenerDatabase()
        repositoryTotals = TotalRecordsRepository(db.totalRecordsDao())
        getTotalRecords = repositoryTotals.getTotalRecords()


    }
    //TOTALS
    fun saveTotal (totalRecords: TotalRecords){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repositoryTotals.insertRecord(totalRecords)
            }
        }

    }

    fun deleteTotal (totalRecords: TotalRecords){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repositoryTotals.deleteRecord(totalRecords)
            }
        }

    }

    fun updateTotal (totalRecords: TotalRecords){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repositoryTotals.updateRecord(totalRecords)
            }
        }

    }

}