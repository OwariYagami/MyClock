package com.overdevx.myclock.data

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room

import kotlinx.coroutines.launch
import java.util.Date

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(application, AlarmDatabase::class.java, "alarm-database").build()
    private val alarmDao = db.alarmDao()

    val alarms: LiveData<List<AlarmEntity>> = alarmDao.getAllAlarms().asLiveData()

    fun addAlarm(title: String, time: Long) {
        viewModelScope.launch {
            alarmDao.insert(AlarmEntity(title = title, time = time))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                setAlarm(getApplication(), time)
            }
        }
    }

    fun deleteAlarm(alarm: AlarmEntity){
        viewModelScope.launch {
            alarmDao.delete(alarm)
        }
    }

    fun toggleAlarm(alarm: AlarmEntity) {
        viewModelScope.launch {
            alarmDao.update(alarm.copy(isEnabled = !alarm.isEnabled))
        }
    }
    @SuppressLint("ScheduleExactAlarm")
    fun setAlarm(context: Context, time: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(AlarmManager::class.java)
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val clockInfo = AlarmManager.AlarmClockInfo(time, pendingIntent)

            if (context.checkSelfPermission(Manifest.permission.SCHEDULE_EXACT_ALARM) == PackageManager.PERMISSION_GRANTED) {
                alarmManager.setAlarmClock(clockInfo, pendingIntent)
            } else {
                // Izin belum diberikan, pengguna harus memberikan izin melalui dialog pengaturan.
            }
        }

    }


    // Metode ekstensi untuk membaca waktu dalam format Date
    fun getAlarmTime(alarmEntity: AlarmEntity): Date {
        return alarmEntity.time.toDate()
    }
}