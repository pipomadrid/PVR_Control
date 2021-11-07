package com.pedrosaez.pvr_control.ui.viewmodel

import androidx.lifecycle.*
import com.pedrosaez.pvr_control.application.App
import com.pedrosaez.pvr_control.database.AppDatabase
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
    val db:AppDatabase = App.obtenerDatabase()

    init {
        repository = OutGoingRepository(db.outGoinsDao())
        getAllOutGoins = repository.getAllOutGoins().asLiveData()
    }


    fun getOutgoin(id:Long): LiveData<List<OutGoins>> {
        val liveData = MutableLiveData<List<OutGoins>>()
        viewModelScope.launch {
            val outGoing = withContext(Dispatchers.IO) {
                repository.getOutGoinsOfPVr(id)
            }
            liveData.postValue(outGoing)
        }
        return liveData
    }

 /*   fun getOutGoinsOfPVr(pvrId:Long):LiveData<List<OutGoins>>{
        return repository.getOutGoinsOfPVr(pvrId).asLiveData()
    }*/

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