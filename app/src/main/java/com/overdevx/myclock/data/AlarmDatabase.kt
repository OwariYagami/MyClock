package com.overdevx.myclock.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [AlarmEntity::class], version = 1, exportSchema = false)
abstract class AlarmDatabase:RoomDatabase() {
    abstract fun alarmDao():AlarmDao

}