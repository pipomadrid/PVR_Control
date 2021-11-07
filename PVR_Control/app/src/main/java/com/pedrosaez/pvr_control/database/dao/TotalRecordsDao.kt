package com.pedrosaez.pvr_control.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pedrosaez.pvr_control.database.entities.PvrAndTotalRecords
import com.pedrosaez.pvr_control.database.entities.TotalRecords



@Dao
abstract class TotalRecordsDao :BaseDao<TotalRecords>(){


    @Transaction
    @Query( "SELECT * FROM datos_pvrs")
    abstract  fun getPvrWithTotalRecords(): LiveData<List<PvrAndTotalRecords>>
    

}