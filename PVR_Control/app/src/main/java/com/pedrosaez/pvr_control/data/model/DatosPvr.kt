package com.pedrosaez.pvr_control.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName= "datosPvrs" )
data class DatosPvr(val nombrePVr:String,val nombreApellidoTitular:String, val direccion:String,val telefono:Int){

    @PrimaryKey(autoGenerate = true)
     var id : Long = 0


}

