package com.example.ownerapp.Messaging

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.ownerapp.R
import com.example.ownerapp.activities.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

private const val CHANNEL_ID = "myChannel1"

class FirebaseService : FirebaseMessagingService() {

    companion object {
        var sharedPrefs: SharedPreferences? = null
        var token: String?
            get() {
                return sharedPrefs?.getString("token", "")
            }
            set(value) {
                sharedPrefs?.edit()?.putString("token", value)?.apply()
            }
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        token = newToken
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)


        val intent = Intent(this, MainActivity::class.java)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManager)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()


        notificationManager.notify(notificationId, notification)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(manager: NotificationManager) {
        val channelName = "myChannel"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "Channel Description"
            enableLights(false)
        }
        manager.createNotificationChannel(channel)
    }
}