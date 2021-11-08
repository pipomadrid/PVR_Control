package com.pedrosaez.pvr_control.repository


import com.pedrosaez.pvr_control.database.dao.DatosPvrDao
import com.pedrosaez.pvr_control.database.entities.DatosPvr
import com.pedrosaez.pvr_control.database.entities.UserAndPvr
import kotlinx.coroutines.flow.Flow

class PvrRepository(private val pvrDao:DatosPvrDao) {


    fun getAllPvr(): Flow<List<UserAndPvr>> = pvrDao.findAll()

    suspend fun insertPvr(pvr:DatosPvr){
        pvrDao.save(pvr)
    }
    suspend fun deletePvr(pvr:DatosPvr){
        pvrDao.delete(pvr)

    }
    suspend fun updatePvr(pvr:DatosPvr){
        pvrDao.update(pvr)
    }


}