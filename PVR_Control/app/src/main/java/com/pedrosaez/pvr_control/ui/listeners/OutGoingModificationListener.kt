package com.pedrosaez.pvr_control.ui.listeners


import com.pedrosaez.pvr_control.database.entities.OutGoins

interface OutGoingModificationListener {



    fun delete(outGoins: OutGoins)

    fun update(outGoins: OutGoins)

    fun create(outGoins: OutGoins)

    fun sendActualOutGoing(outGoins: OutGoins)

}