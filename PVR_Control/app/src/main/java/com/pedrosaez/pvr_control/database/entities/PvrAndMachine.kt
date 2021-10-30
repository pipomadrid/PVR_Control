package com.pedrosaez.pvr_control.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class PvrAndMachine(
    @Embedded val pvr: DatosPvr,
    @Relation(
        parentColumn = "id",
        entityColumn = "pvr_id"
    )
    val machine: PvrMachine
)
