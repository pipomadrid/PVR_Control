package com.pedrosaez.pvr_control.database.entities

import androidx.room.Embedded
import androidx.room.ForeignKey
import androidx.room.Relation

data class PvrAndParcialRecords(
        @Embedded val pvr: DatosPvr,
        @Relation(
                parentColumn = "id",
                entityColumn = "pvr_id"
        )
        val parcialRecords: List<ParcialRecords>
)
