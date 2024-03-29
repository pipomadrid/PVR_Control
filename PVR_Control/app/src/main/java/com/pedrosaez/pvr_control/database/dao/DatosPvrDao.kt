package com.pedrosaez.pvr_control.database.dao


import androidx.room.*
import com.pedrosaez.pvr_control.database.entities.DatosPvr
import com.pedrosaez.pvr_control.database.entities.UserAndPvr
import kotlinx.coroutines.flow.Flow


@Dao
abstract class DatosPvrDao:BaseDao<DatosPvr>() {

    @Transaction
    @Query(value = "SELECT * FROM user")
    abstract  fun findAll(): Flow<List<UserAndPvr>>

}