package com.pedrosaez.pvr_control.ui.viewmodel


import androidx.lifecycle.*
import com.pedrosaez.pvr_control.application.App
import com.pedrosaez.pvr_control.database.entities.DatosPvr
import com.pedrosaez.pvr_control.database.entities.PvrAndMachine
import com.pedrosaez.pvr_control.database.entities.PvrMachine
import com.pedrosaez.pvr_control.repository.MachineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MachineViewModel:ViewModel() {


    private val repository : MachineRepository
    val getMachine : LiveData<List<PvrMachine>>


    init{
        val db = App.obtenerDatabase().machinePvrDao()
        repository = MachineRepository(db)
        getMachine = repository.getMachines().asLiveData()
    }



    // obtenemos los datos de la maquina en un livedata pra observar los cambios
 /*   fun getMachine(pvrId:Long) :LiveData<PvrMachine>{
        val liveData = MutableLiveData<PvrMachine>()
        viewModelScope.launch {
           val machine = withContext(Dispatchers.IO) {
                 repository.getMachineFromPvr(pvrId)
              }
            liveData.postValue(machine)
            }
        return liveData
        }
*/

    fun save (pvrMachine:PvrMachine){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertMachine(pvrMachine)
            }
        }

    }

    fun delete (pvrMachine:PvrMachine){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteMachine(pvrMachine)
            }
        }

    }

    fun update (pvrMachine:PvrMachine){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateMachine(pvrMachine)
            }
        }

    }

}