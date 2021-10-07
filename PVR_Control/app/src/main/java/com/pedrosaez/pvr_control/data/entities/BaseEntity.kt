package com.pedrosaez.pvr_control.data.entities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.util.*

//Clase padre de las entities para no repetir codigo
abstract class BaseEntity {

    @PrimaryKey(autoGenerate = true)
    var id : Long = 0

    @ColumnInfo(name = "create_at")
    var createAt: Date = Date(System.currentTimeMillis())

    @ColumnInfo(name = "update_at")
    var updateAt: Date = Date(System.currentTimeMillis())
}