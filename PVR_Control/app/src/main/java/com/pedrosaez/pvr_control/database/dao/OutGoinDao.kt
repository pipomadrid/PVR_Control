package com.pedrosaez.pvr_control.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.pedrosaez.pvr_control.database.entities.OutGoins
import com.pedrosaez.pvr_control.database.entities.ParcialRecords
import com.pedrosaez.pvr_control.database.entities.PvrAndOutGoins
import com.pedrosaez.pvr_control.database.entities.PvrAndParcialRecords
import kotlinx.coroutines.flow.Flow


@Dao
abstract class OutGoinDao : BaseDao<OutGoins>() {


    @Transaction
    @Query("SELECT * FROM datos_pvrs")
    abstract fun getPvrWithOutGoins(): Flow<List<PvrAndOutGoins>>

    @Transaction
    @Query("SELECT * FROM out_goins WHERE pvr_id =:pvrId ")
    abstract fun getOutgoinsOfPVr(pvrId:Long): List<OutGoins>

}
