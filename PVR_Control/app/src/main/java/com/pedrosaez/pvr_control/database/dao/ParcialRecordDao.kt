package com.pedrosaez.pvr_control.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.pedrosaez.pvr_control.database.entities.ParcialRecords
import com.pedrosaez.pvr_control.database.entities.PvrAndParcialRecords
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ParcialRecordDao :BaseDao<ParcialRecords>(){


    @Transaction
    @Query( "SELECT * FROM datos_pvrs")
    abstract fun getPvrWithParcialRecords(): LiveData<List<PvrAndParcialRecords>>

    @Transaction
    @Query( "SELECT * FROM datos_pvrs WHERE id =:id")
    abstract suspend fun getRecordsFromPvr(id:Long): List<ParcialRecords>

}