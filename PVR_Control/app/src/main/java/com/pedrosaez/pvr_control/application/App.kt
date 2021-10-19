package com.pedrosaez.pvr_control.application

import android.app.Application
import com.pedrosaez.pvr_control.database.AppDatabase

class App :Application(){



    companion object {
        private var database:AppDatabase?=null
        fun obtenerDatabase(): AppDatabase{
            return database!!
        }
    }

    override fun onCreate() {
        super.onCreate()
         database = AppDatabase.createDb(applicationContext)
    }

}