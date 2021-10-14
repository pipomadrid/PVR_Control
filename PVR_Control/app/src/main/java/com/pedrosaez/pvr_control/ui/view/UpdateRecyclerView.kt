package com.pedrosaez.pvr_control.ui.view

import com.pedrosaez.pvr_control.data.entities.DatosPvr

//Interfaz para borrar registro del pvr de  la base de datos
interface UpdateRecyclerView {
    fun delete(pvr:DatosPvr)

    fun update(pvr:DatosPvr)

    fun create(pvr:DatosPvr)
}