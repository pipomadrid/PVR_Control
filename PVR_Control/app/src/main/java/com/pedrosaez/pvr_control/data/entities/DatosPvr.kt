package com.pedrosaez.pvr_control.data.entities

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*


@Entity(tableName= "datos_pvrs" )
data class DatosPvr(@ColumnInfo(name = "pvr_name") var pvrName:String, @ColumnInfo(name = "name_surname") var nameSurname:String,
                    var address:String, var phone:String, @Nullable var authDate: Date? ):BaseEntity(){


}

