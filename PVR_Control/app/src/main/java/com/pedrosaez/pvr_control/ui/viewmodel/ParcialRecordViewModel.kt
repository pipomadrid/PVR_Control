package com.pedrosaez.pvr_control.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pedrosaez.pvr_control.application.App
import com.pedrosaez.pvr_control.database.entities.ParcialRecords
import com.pedrosaez.pvr_control.database.entities.PvrAndParcialRecords
import com.pedrosaez.pvr_control.repository.ParcialRecordsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ParcialRecordViewModel:ViewModel() {

    private val repositoryParcials : ParcialRecordsRepository
    val getParcialRecords : LiveData<List<PvrAndParcialRecords>>


    init{
        val db= App.obtenerDatabase()
        repositoryParcials = ParcialRecordsRepository(db.parcialRecordsDao())
        getParcialRecords = repositoryParcials.getParcialRecords()

    }


    //PARCIALS
    fun saveParcial (parcialRecords: ParcialRecords){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repositoryParcials.insertRecord(parcialRecords)
            }
        }

    }

    fun deleteParcial (parcialRecords: ParcialRecords){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repositoryParcials.deleteRecord(parcialRecords)
            }
        }

    }

    fun updateParcial (parcialRecords: ParcialRecords) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repositoryParcials.updateRecord(parcialRecords)
            }
        }
    }

    val getAllParcials:LiveData<List<ParcialRecords>> = repositoryParcials.getAllParcial().asLiveData()

}