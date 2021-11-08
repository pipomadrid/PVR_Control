package com.pedrosaez.pvr_control.database.dao

import androidx.room.*
import com.pedrosaez.pvr_control.database.entities.User
import com.pedrosaez.pvr_control.database.entities.UserAndPvr
import kotlinx.coroutines.flow.Flow


@Dao
abstract class UserDao:BaseDao<User>() {

    @Query(value = "SELECT * FROM user")
    abstract  fun findAllUser(): Flow<List<User>>
}