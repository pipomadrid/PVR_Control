package com.pedrosaez.pvr_control.data.dao

import androidx.room.*
import com.pedrosaez.pvr_control.data.entities.DatosPvr

@Dao
abstract class DatosPvrDao:BaseDao<DatosPvr>() {

    @Query(value = "SELECT * FROM datos_pvrs")
    abstract suspend fun findAll():MutableList<DatosPvr>

}