package com.pedrosaez.pvr_control.repository


import com.pedrosaez.pvr_control.database.dao.UserDao
import com.pedrosaez.pvr_control.database.entities.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {


    fun getAllUser(): Flow<List<User>> = userDao.findAllUser()



    suspend fun insertUser(user: User){
        userDao.save(user)
    }
    suspend fun deleteUser(user: User){
        userDao.delete(user)

    }
    suspend fun updateUser(user: User){
        userDao.update(user)
    }

}