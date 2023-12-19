package com.example.StickToMyWish

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class AlarmActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val perms = arrayOf("android.permission.POST_NOTIFICATIONS")
            ActivityCompat.requestPermissions(this, perms, 0)
            return
        }
        val intent = Intent(this, MainActivity2::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val channelId = createNotificationChannel(
            "my_channel_ID", "my_channel_NAME", NotificationManager.IMPORTANCE_HIGH
        )
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(
            this, channelId!!
        ).setContentTitle("notification").setContentText("alarm!").setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(100, notification.build())
    }

    private fun createNotificationChannel(
        channelID: String, channelNAME: String, level: Int
    ): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(channelID, channelNAME, level)
            manager.createNotificationChannel(channel)
            channelID
        } else {
            null
        }
    }


}