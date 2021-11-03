package com.pedrosaez.pvr_control.database.entities


import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "records")
data class Records (var sells:Int, var bills:Int, var coins:Int, @ColumnInfo(name = "pvr_id") val pvrId:Long):BaseEntity()