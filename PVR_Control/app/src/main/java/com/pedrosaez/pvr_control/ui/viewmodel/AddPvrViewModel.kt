package com.pedrosaez.pvr_control.ui.viewmodel

import androidx.lifecycle.*
import com.pedrosaez.pvr_control.application.App
import com.pedrosaez.pvr_control.database.entities.DatosPvr
import com.pedrosaez.pvr_control.repository.PvrRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPvrViewModel:ViewModel() {

    private val repository :PvrRepository
    val getAll_Pvr : LiveData<List<DatosPvr>>

    init{
         val db = App.obtenerDatabase().datosPvrDao()
        repository = PvrRepository(db)
        getAll_Pvr = repository.getAllPvr().asLiveData()
    }


    fun save(pvr: DatosPvr) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertPvr(pvr)
            }
        }

    }

    fun update(pvr: DatosPvr) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
               repository.updatePvr(pvr)
            }
        }

    }
    fun delete(pvr: DatosPvr) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deletePvr(pvr)
            }
        }

    }




}
