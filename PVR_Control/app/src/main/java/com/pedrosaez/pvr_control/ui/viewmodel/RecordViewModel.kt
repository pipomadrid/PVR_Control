package com.pedrosaez.pvr_control.ui.viewmodel

import android.icu.text.AlphabeticIndex
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pedrosaez.pvr_control.application.App
import com.pedrosaez.pvr_control.database.entities.PvrAndRecords
import com.pedrosaez.pvr_control.database.entities.PvrMachine
import com.pedrosaez.pvr_control.database.entities.Records
import com.pedrosaez.pvr_control.repository.MachineRepository
import com.pedrosaez.pvr_control.repository.RecordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecordViewModel:ViewModel(){
    private val repository : RecordRepository
    val getParcialRecords : LiveData<MutableList<PvrAndRecords>>
    val getTotalRecords : LiveData<List<PvrAndRecords>>

    init{
        val db = App.obtenerDatabase().recordsDao()
        repository = RecordRepository(db)
        getParcialRecords = repository.getRecords().asLiveData()
        getTotalRecords = repository.getRecords().asLiveData()

    }

    fun save (records: Records){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertRecord(records)
            }
        }

    }

    fun delete (records: Records){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteRecord(records)
            }
        }

    }

    fun update (records: Records){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateRecord(records)
            }
        }

    }
}