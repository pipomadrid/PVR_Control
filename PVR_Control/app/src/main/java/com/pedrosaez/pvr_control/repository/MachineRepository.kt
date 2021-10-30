package com.pedrosaez.pvr_control.repository

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.dao.MachineDao
import com.pedrosaez.pvr_control.database.entities.DatosPvr
import com.pedrosaez.pvr_control.database.entities.PvrAndMachine
import com.pedrosaez.pvr_control.database.entities.PvrMachine
import kotlinx.coroutines.flow.Flow


class MachineRepository(private val machineDao :MachineDao):Application(){




    fun getMachines(): Flow<List<PvrMachine>> = machineDao.findPvrAndMachines()

    fun getMachineFromPvr(pvrId:Long): Flow<PvrMachine> = machineDao.finByIdPvrAndMachines(pvrId)


    suspend fun insertMachine(pvrMachine: PvrMachine){
        machineDao.save(pvrMachine)
    }
    suspend fun deleteMachine(pvrMachine: PvrMachine){
        machineDao.delete(pvrMachine)

    }
    suspend fun updateMachine(pvrMachine: PvrMachine){
        machineDao.update(pvrMachine)
    }
}