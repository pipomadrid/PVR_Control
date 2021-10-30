package com.pedrosaez.pvr_control.database.dao


import androidx.room.*
import com.pedrosaez.pvr_control.database.entities.DatosPvr
import kotlinx.coroutines.flow.Flow


@Dao
abstract class DatosPvrDao:BaseDao<DatosPvr>() {

    @Query(value = "SELECT * FROM datos_pvrs")
    abstract  fun findAll(): Flow<List<DatosPvr>>

}