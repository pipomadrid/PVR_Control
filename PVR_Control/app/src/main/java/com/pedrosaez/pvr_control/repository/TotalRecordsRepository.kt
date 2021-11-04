package com.pedrosaez.pvr_control.repository

import androidx.lifecycle.LiveData
import com.pedrosaez.pvr_control.database.dao.TotalRecordsDao
import com.pedrosaez.pvr_control.database.entities.PvrAndTotalRecords
import com.pedrosaez.pvr_control.database.entities.TotalRecords
import kotlinx.coroutines.flow.Flow

class TotalRecordsRepository(private val recordsDao: TotalRecordsDao) {


    fun getTotalRecords(): LiveData<List<PvrAndTotalRecords>> = recordsDao.getPvrWithTotalRecords()


    suspend fun insertRecord(totalRecords: TotalRecords){
        recordsDao.save(totalRecords)
    }
    suspend fun deleteRecord(totalRecords: TotalRecords){
        recordsDao.delete(totalRecords)

    }
    suspend fun updateRecord(totalRecords: TotalRecords){
        recordsDao.update(totalRecords)
    }
}