package com.pedrosaez.pvr_control.database.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "total_records",
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = DatosPvr::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("pvr_id"),
                        onDelete = ForeignKey.CASCADE
                ),

        ))
data class TotalRecords (var sells:Long, var bills:Long, var coins:Long,var money:Long, @ColumnInfo(name = "pvr_id") val pvrId:Long):BaseEntity()