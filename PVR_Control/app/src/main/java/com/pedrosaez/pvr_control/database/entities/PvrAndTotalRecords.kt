package com.pedrosaez.pvr_control.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class PvrAndTotalRecords (
    @Embedded val pvr: DatosPvr,
    @Relation(
            parentColumn = "id",
            entityColumn = "pvr_id"
    )
    val totalRecords: List<TotalRecords>
)