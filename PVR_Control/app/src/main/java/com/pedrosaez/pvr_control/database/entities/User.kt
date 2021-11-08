package com.pedrosaez.pvr_control.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index


@Entity(tableName = "user",indices = arrayOf(Index(value = ["uid"],
        unique = true)))
data class User(val email:String, @ColumnInfo(name = "uid")val uid:String):BaseEntity()