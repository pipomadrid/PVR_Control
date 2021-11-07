package com.pedrosaez.pvr_control.repository

import androidx.lifecycle.LiveData
import com.pedrosaez.pvr_control.database.dao.OutGoinDao
import com.pedrosaez.pvr_control.database.entities.OutGoins
import com.pedrosaez.pvr_control.database.entities.PvrAndOutGoins
import kotlinx.coroutines.flow.Flow


class OutGoingRepository(private val recordsDao: OutGoinDao) {


    fun getAllOutGoins(): Flow<List<PvrAndOutGoins>> = recordsDao.getPvrWithOutGoins()

    fun getOutGoinsOfPVr(pvrId:Long): List<OutGoins> = recordsDao.getOutgoinsOfPVr(pvrId)


    suspend fun insertOutGoin(outGoins: OutGoins){
        recordsDao.save(outGoins)
    }
    suspend fun deleteOutGoin(outGoins: OutGoins){
        recordsDao.delete(outGoins)

    }
    suspend fun updateOutGoin(outGoins: OutGoins){
        recordsDao.update(outGoins)
    }
}