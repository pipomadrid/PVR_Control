package com.pedrosaez.pvr_control.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName= "pvr_machine" )
data class PvrMachine(var brand:String, var model:String, @ColumnInfo(name = "serial_number") var serialNumber:String,
                      @ColumnInfo(name = "rails_number") var railsNumber:Int, @ColumnInfo(name = "pvr_id")val pvrId:Long):BaseEntity()
