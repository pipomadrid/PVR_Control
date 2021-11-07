package com.pedrosaez.pvr_control.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.*


@Entity(tableName = "out_goins",
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = DatosPvr::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("pvr_id"),
                        onDelete = ForeignKey.CASCADE
                )
        ))
data class OutGoins(var cost: Int, var description: String, var date: Date, @ColumnInfo(name = "pvr_id") val pvrId:Long) : BaseEntity() {
}