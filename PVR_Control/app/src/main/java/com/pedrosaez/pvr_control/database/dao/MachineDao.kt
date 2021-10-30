package com.pedrosaez.pvr_control.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.pedrosaez.pvr_control.database.entities.PvrMachine
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MachineDao: BaseDao<PvrMachine>() {

    @Transaction
    @Query("SELECT * FROM pvr_machine")
    abstract fun findPvrAndMachines():Flow<List<PvrMachine>>

    @Transaction
    @Query("SELECT * FROM pvr_machine where pvr_id=:pvrId")
    abstract fun finByIdPvrAndMachines(pvrId:Long):Flow<PvrMachine>

}