package com.example.medhelper.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.medhelper.R // Assuming you have a drawable for the icon

class AlarmReceiver : BroadcastReceiver() {

    private val CHANNEL_ID = "medication_reminder_channel"
    private val NOTIFICATION_ID = 101

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val medicationName = intent?.getStringExtra("MEDICATION_NAME") ?: "Medication"
            val medicationDosage = intent?.getStringExtra("MEDICATION_DOSAGE") ?: ""

            createNotificationChannel(it)

            val builder = NotificationCompat.Builder(it, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app icon
                .setContentTitle("Medication Reminder")
                .setContentText("Time to take your $medicationName ($medicationDosage)")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(it)) {
                if (areNotificationsEnabled()) {
                    notify(NOTIFICATION_ID, builder.build())
                }
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Medication Reminders"
            val descriptionText = "Notifications for medication schedules"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = 
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}