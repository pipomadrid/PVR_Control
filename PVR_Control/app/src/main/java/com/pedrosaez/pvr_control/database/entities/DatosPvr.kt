package com.pedrosaez.pvr_control.database.entities


import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import java.io.Serializable
import java.util.*


@Entity(tableName = "datos_pvrs",
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = User::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("user_id"),
                        onDelete = CASCADE
                ))
)
data class DatosPvr(@ColumnInfo(name = "pvr_name") var pvrName: String, @ColumnInfo(name = "name_surname") var nameSurname: String,
                    var address: String, var phone: String, @Nullable var authDate: Date?, @ColumnInfo(name = "user_id") val userId: Long) : BaseEntity(), Serializable




