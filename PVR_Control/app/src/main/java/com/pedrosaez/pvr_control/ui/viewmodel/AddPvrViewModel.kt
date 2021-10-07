package com.pedrosaez.pvr_control.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedrosaez.pvr_control.application.App
import com.pedrosaez.pvr_control.data.entities.DatosPvr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPvrViewModel:ViewModel() {

    val db = App.obtenerDatabase()


    fun save(pvr:DatosPvr){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.datosPvrDao().save(pvr)
            }
        }

    }
}