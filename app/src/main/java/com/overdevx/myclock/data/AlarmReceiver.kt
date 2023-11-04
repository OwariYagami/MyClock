package com.overdevx.myclock.data

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.overdevx.myclock.R

class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(context)
        val notification = Notification.Builder(context, "alarm_channel")
            .setContentTitle("Alarm")
            .setContentText("Time to wake up!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        notificationManager.notify(0, notification)
    }
    private fun createNotificationChannel(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val name = "Alarm Channel"
            val descriptionText = "Channel for alarms"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("alarm_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
