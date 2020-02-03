package ru.shrott.shrottmaster.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.shrott.shrottmaster.R
import ru.shrott.shrottmaster.view.activities.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        try {
            sendNotification(remoteMessage?.notification?.body, remoteMessage?.data?.get("message_id"))
        } catch (ignored: NullPointerException) {

        }
    }

    private fun sendNotification(messageBody: String?, messageId: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("messageId", messageId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_action_percent)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
            .setContentTitle(this.getString(R.string.app_name))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setLights(Color.MAGENTA, 500, 1000)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(
            0 /* ID of notification */,
            notificationBuilder.build()
        )


    }
}