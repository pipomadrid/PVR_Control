package com.pedrosaez.pvr_control.database

import android.content.Context
import androidx.room.*
import com.pedrosaez.pvr_control.database.converters.Converters
import com.pedrosaez.pvr_control.database.dao.DatosPvrDao
import com.pedrosaez.pvr_control.database.dao.MachineDao
import com.pedrosaez.pvr_control.database.dao.RecordsDao
import com.pedrosaez.pvr_control.database.entities.DatosPvr
import com.pedrosaez.pvr_control.database.entities.PvrMachine
import com.pedrosaez.pvr_control.database.entities.Records

@Database(entities = [DatosPvr::class,PvrMachine::class,Records::class],version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase :RoomDatabase() {

        abstract fun datosPvrDao():DatosPvrDao
        abstract fun machinePvrDao():MachineDao
        abstract fun recordsDao():RecordsDao

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