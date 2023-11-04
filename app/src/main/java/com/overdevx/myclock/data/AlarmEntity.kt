package com.overdevx.myclock.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val title:String,
    val time:Long,
    val isEnabled: Boolean = true
)

fun Long.toDate(): Date {
    return Date(this)
}

fun Date.toLong(): Long {
    return time
}
