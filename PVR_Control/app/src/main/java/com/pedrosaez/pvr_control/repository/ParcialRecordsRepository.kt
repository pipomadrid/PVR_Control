package com.pedrosaez.pvr_control.repository

import androidx.lifecycle.LiveData
import com.pedrosaez.pvr_control.database.dao.ParcialRecordDao
import com.pedrosaez.pvr_control.database.entities.ParcialRecords
import com.pedrosaez.pvr_control.database.entities.PvrAndParcialRecords
import kotlinx.coroutines.flow.Flow


class ParcialRecordsRepository(private val recordsDao: ParcialRecordDao) {


    fun getParcialRecords(): LiveData<List<PvrAndParcialRecords>> = recordsDao.getPvrWithParcialRecords()

    fun getAllParcial(): Flow<List<ParcialRecords>> = recordsDao.getAllParcialRecords()


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