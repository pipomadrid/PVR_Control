package com.pedrosaez.pvr_control.ui.listeners

import com.pedrosaez.pvr_control.database.entities.DatosPvr

//Interfaz para borrar registro del pvr de  la base de datos
interface  PvrModificationListener{

    fun delete(pvr:DatosPvr)

    fun update(pvr:DatosPvr)

    fun create(pvr:DatosPvr)

    fun sendActualPvr(pvr:DatosPvr)



}