package com.pedrosaez.pvr_control.database.entities

import androidx.room.Embedded
import androidx.room.Relation


data class UserAndPvr(
        @Embedded val user: User,
        @Relation(
                parentColumn = "id",
                entityColumn = "user_id"
        )
        val DatosPvr: List<DatosPvr>
)

