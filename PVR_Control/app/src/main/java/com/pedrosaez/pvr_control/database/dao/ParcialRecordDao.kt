package com.pedrosaez.pvr_control.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pedrosaez.pvr_control.database.entities.ParcialRecords
import com.pedrosaez.pvr_control.database.entities.PvrAndParcialRecords
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ParcialRecordDao :BaseDao<ParcialRecords>(){


    @Transaction
    @Query( "SELECT * FROM datos_pvrs")
    abstract fun getPvrWithParcialRecords(): LiveData<List<PvrAndParcialRecords>>

    @Query( "SELECT * FROM parcial_records")
    abstract fun getAllParcialRecords() :Flow<List<ParcialRecords>>



}