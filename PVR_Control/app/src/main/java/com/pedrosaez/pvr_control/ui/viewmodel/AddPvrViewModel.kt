package com.pedrosaez.pvr_control.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedrosaez.pvr_control.application.App
import com.pedrosaez.pvr_control.data.entities.DatosPvr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPvrViewModel:ViewModel() {

    val db = App.obtenerDatabase()


    fun save(pvr: DatosPvr) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.datosPvrDao().save(pvr)
            }
        }

    }

    val _pvr_list: MutableLiveData<List<DatosPvr>> by lazy {
        // also --> Quiero hacer algo más, en este caso introducir datos
        MutableLiveData<List<DatosPvr>>().also {
            loadDatosPvr()
        }
    }

    fun getPvr():LiveData<List<DatosPvr>> {
        return _pvr_list
    }

    private fun loadDatosPvr() {
        //SIMULANDO LA CONEXIÓN A LA BD
        viewModelScope.launch {
            withContext(Dispatchers.IO) { //CONTEXTO DE ENTRADA Y SALIDA
                var pvr_list = db.datosPvrDao().findAll()
                _pvr_list.postValue(pvr_list)  //MAIN THREAD
            }

        }

    }

    fun delete(pvr: DatosPvr) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.datosPvrDao().delete(pvr)
            }
        }

    }
}
