package com.pedrosaez.pvr_control.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pedrosaez.pvr_control.application.App
import com.pedrosaez.pvr_control.database.entities.DatosPvr
import com.pedrosaez.pvr_control.database.entities.OutGoins
import com.pedrosaez.pvr_control.database.entities.ParcialRecords
import com.pedrosaez.pvr_control.database.entities.PvrAndOutGoins
import com.pedrosaez.pvr_control.repository.OutGoingRepository
import com.pedrosaez.pvr_control.repository.PvrRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OutGoinViewModel:ViewModel() {


    private val repository: OutGoingRepository
    val getAllOutGoins: LiveData<List<PvrAndOutGoins>>


    init {
        val db = App.obtenerDatabase().outGoinsDao()
        repository = OutGoingRepository(db)
        getAllOutGoins = repository.getAllOutGoins().asLiveData()
    }

    fun getOutGoinsOfPVr(pvrId:Long):LiveData<List<OutGoins>>{
        return repository.getOutGoinsOfPVr(pvrId).asLiveData()
    }

    fun saveOutGoin(outGoins: OutGoins) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertOutGoin(outGoins)
            }
        }

    }

    fun deleteOutGoin(outGoins: OutGoins) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteOutGoin(outGoins)
            }
        }

    }

    fun updateOutGoin(outGoins: OutGoins) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateOutGoin(outGoins)
            }
        }


    }
}