package com.example.medhelper.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.medhelper.data.local.Medication
import java.util.Calendar

class AlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleAlarm(medication: Medication) {
        // Parse schedule string (e.g., "Daily, 8 AM") to get time
        // This is a simplified example. A robust solution would parse more complex schedules.
        val parts = medication.schedule.split(", ")
        if (parts.size < 2) return // Invalid schedule format

        val timePart = parts[1] // e.g., "8 AM"
        val hour = timePart.substringBefore(" ").toIntOrNull() ?: return
        val amPm = timePart.substringAfter(" ")

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, if (amPm == "PM" && hour != 12) hour + 12 else hour)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // If the alarm time has already passed for today, schedule for tomorrow
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("MEDICATION_NAME", medication.name)
            putExtra("MEDICATION_DOSAGE", medication.dosage)
            // Use a unique request code for each medication alarm
            // For simplicity, using medication.id. A more robust solution might use a combination.
            data = android.net.Uri.parse("medhelper://medication/${medication.id}")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medication.id, // Request code
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule repeating alarm (e.g., daily)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, // Repeat daily
            pendingIntent
        )
    }

    fun cancelAlarm(medicationId: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medicationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}