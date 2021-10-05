package com.pedrosaez.pvr_control.data.dao

import androidx.room.*
import com.pedrosaez.pvr_control.data.model.DatosPvr

@Dao
interface DatosPvrDao {

    @Query("SELECT * from datosPvrs")
    suspend fun  findAll():List<DatosPvr>

    @Insert
    suspend fun save(datosPvr: DatosPvr):Long

    @Update
    suspend fun update(datosPvr:DatosPvr)

    @Delete
    suspend fun delete(datosPvr:DatosPvr)


}