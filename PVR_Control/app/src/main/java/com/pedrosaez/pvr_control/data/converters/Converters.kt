package com.pedrosaez.pvr_control.data.converters

import androidx.room.TypeConverter
import java.util.*


//calse para convertir datos para pasarlos a la base de datos

class Converters {

    //convertir long en date al recuperar de base de datos
    @TypeConverter
    fun toDate(date:Long?): Date? = date?.let{
            Date(it)
    }
    //convertir tipo date en Long al guardar en base de datos
    @TypeConverter
    fun toLong(date:Date): Long = date.time

}