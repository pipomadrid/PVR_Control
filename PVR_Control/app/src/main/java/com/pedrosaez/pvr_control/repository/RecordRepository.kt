package com.pedrosaez.pvr_control.repository

import com.pedrosaez.pvr_control.database.dao.RecordsDao
import com.pedrosaez.pvr_control.database.entities.PvrAndRecords
import com.pedrosaez.pvr_control.database.entities.Records
import kotlinx.coroutines.flow.Flow

class RecordRepository(private val recordsDao: RecordsDao) {


    fun getRecords(): Flow<MutableList<PvrAndRecords>> = recordsDao.getPvrWithRecords()


    suspend fun insertRecord(records: Records){
        recordsDao.save(records)
    }
    suspend fun deleteRecord(records: Records){
        recordsDao.delete(records)

    }
    suspend fun updateRecord(records: Records){
        recordsDao.update(records)
    }
}