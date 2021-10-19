package com.pedrosaez.pvr_control.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.pedrosaez.pvr_control.database.entities.BaseEntity
import java.util.*


//clase padre de las clases dao  para no repetir codigo
abstract class BaseDao<T> where T:BaseEntity{


    @Delete
    abstract fun delete(t: T)

    @Insert
    abstract fun save(t: T)

    @Update
    protected abstract fun updateProtect(t: T)

    fun update(t: T) {
        t.updateAt = Date(System.currentTimeMillis())
        updateProtect(t)
    }

}