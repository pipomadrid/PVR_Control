package com.pedrosaez.pvr_control.data

import android.content.Context
import androidx.room.*
import com.pedrosaez.pvr_control.data.converters.Converters
import com.pedrosaez.pvr_control.data.dao.DatosPvrDao
import com.pedrosaez.pvr_control.data.entities.DatosPvr

@Database(entities = arrayOf(DatosPvr::class),version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase :RoomDatabase() {

        abstract fun datosPvrDao():DatosPvrDao

        companion object{
            private var instance: AppDatabase? = null
            private const val NAME_DB = "pvr_control_db"


            fun createDb(context: Context):AppDatabase{
                //patron singleton para que solo se instancie una vez
                if(instance == null){
                    instance = Room.databaseBuilder(context,AppDatabase::class.java, NAME_DB).build()
                }

                return instance!!

            }

        }

}