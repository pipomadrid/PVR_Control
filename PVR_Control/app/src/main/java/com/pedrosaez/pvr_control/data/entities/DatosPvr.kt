package com.pedrosaez.pvr_control.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*


@Entity(tableName= "datosPvrs" )
data class DatosPvr(@ColumnInfo(name = "pvr_name") val pvrName:String, @ColumnInfo(name = "name_surname") val nameSurname:String,
                    val address:String, val phone:Int, val authDate: Date):BaseEntity(){

}

