package com.pedrosaez.pvr_control.repository

import androidx.lifecycle.LiveData
import com.pedrosaez.pvr_control.database.dao.ParcialRecordDao
import com.pedrosaez.pvr_control.database.entities.ParcialRecords
import com.pedrosaez.pvr_control.database.entities.PvrAndParcialRecords
import kotlinx.coroutines.flow.Flow

class ParcialRecordsRepository(private val recordsDao: ParcialRecordDao) {


    fun getParcialRecords(): LiveData<List<PvrAndParcialRecords>> = recordsDao.getPvrWithParcialRecords()


    suspend fun getRecordsFromPvr(id:Long): List<ParcialRecords> = recordsDao.getRecordsFromPvr(id)

    suspend fun insertRecord(parcialRecords: ParcialRecords){
        recordsDao.save(parcialRecords)
    }
    suspend fun deleteRecord(parcialRecords: ParcialRecords){
        recordsDao.delete(parcialRecords)

    }
    suspend fun updateRecord(parcialRecords: ParcialRecords){
        recordsDao.update(parcialRecords)
    }
}