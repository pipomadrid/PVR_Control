package com.pedrosaez.pvr_control.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pedrosaez.pvr_control.data.dao.DatosPvrDao
import com.pedrosaez.pvr_control.data.model.DatosPvr

@Database(entities = arrayOf(DatosPvr::class),version = 1)
abstract class AppDatabase :RoomDatabase() {

        abstract fun datosPvrDao():DatosPvrDao

        companion object{
            private var instance: AppDatabase? = null
            private const val NAME_DB = "pvr_control_db"

            fun createDb(context: Context):AppDatabase{
                if(instance == null){
                    instance = Room.databaseBuilder(context,AppDatabase::class.java, NAME_DB).build()
                }

                return instance!!

            }

        }

}