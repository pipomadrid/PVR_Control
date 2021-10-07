package com.pedrosaez.pvr_control.data.dao

import androidx.room.*
import com.pedrosaez.pvr_control.data.entities.DatosPvr

@Dao
abstract class DatosPvrDao:BaseDao<DatosPvr>() {

    @Query("SELECT * from datosPvrs")
    abstract suspend fun  findAll():List<DatosPvr>



}