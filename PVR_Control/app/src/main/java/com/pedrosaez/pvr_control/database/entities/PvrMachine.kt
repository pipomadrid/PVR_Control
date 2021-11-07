package com.pedrosaez.pvr_control.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(tableName= "pvr_machine",
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = DatosPvr::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("pvr_id"),
                        onDelete = ForeignKey.CASCADE
                )
        ))
data class PvrMachine(var brand:String, var model:String, @ColumnInfo(name = "serial_number") var serialNumber:String,
                      @ColumnInfo(name = "rails_number") var railsNumber:Int, @ColumnInfo(name = "pvr_id")val pvrId:Long):BaseEntity()
