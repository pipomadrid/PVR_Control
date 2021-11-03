package com.pedrosaez.pvr_control.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.pedrosaez.pvr_control.database.entities.PvrAndRecords
import com.pedrosaez.pvr_control.database.entities.Records
import kotlinx.coroutines.flow.Flow


@Dao
abstract class RecordsDao :BaseDao<Records>(){


    @Transaction
    @Query( "SELECT * FROM datos_pvrs")
    abstract  fun getPvrWithRecords(): Flow<MutableList<PvrAndRecords>>

}