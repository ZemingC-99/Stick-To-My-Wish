package com.example.StickToMyWish

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Calendar


class AlarmReceiver : BroadcastReceiver() {

    lateinit var context: Context;

    override fun onReceive(context: Context, intent: Intent?) {
        this.context = context

        val intent1 = Intent(context, MainActivity2::class.java)
        intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0)
        val channelId = createNotificationChannel(
            "my_channel_ID", "my_channel_NAME", NotificationManager.IMPORTANCE_HIGH
        )
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(
            context, channelId!!
        ).setContentTitle("stick to your wish！").setContentText("alarm!").setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                this.context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            if (MyApplication.notificationAble && MyApplication.notification == "day") {
                notificationManager.notify(100, notification.build())
                addAlarm(1000 * 60 * 60 * 24)
            } else if (MyApplication.notificationAble && MyApplication.notification == "hour") {
                notificationManager.notify(100, notification.build())
                addAlarm(1000 * 60 * 60)
            }
        }

    }

    private fun createNotificationChannel(
        channelID: String, channelNAME: String, level: Int
    ): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager =
                context.getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(channelID, channelNAME, level)
            manager.createNotificationChannel(channel)
            channelID
        } else {
            null
        }
    }

    private fun addAlarm(time: Int) {
        val alarmManager: AlarmManager;
        alarmManager = this.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager;

        val calendar: Calendar = Calendar.getInstance()
        val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minute: Int = calendar.get(Calendar.MINUTE)
        val c: Calendar = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, hour)
        c.set(Calendar.MINUTE, minute)
        val intent = Intent(this.context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this.context, 0X102, intent, 0)
        alarmManager.set(
            AlarmManager.RTC_WAKEUP, c.getTimeInMillis() - 1000 * 60 + time, pendingIntent
        )
    }

}