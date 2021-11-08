package com.pedrosaez.pvr_control.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity (tableName = "parcial_records",
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = DatosPvr::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("pvr_id"),
                        onDelete = ForeignKey.CASCADE
                )
        ))
data class ParcialRecords (var sells:Long, var bills:Long, var coins:Double,var money:Double, @ColumnInfo(name = "pvr_id") val pvrId:Long):BaseEntity()
