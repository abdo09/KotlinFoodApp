package net.ferraSolution.food.components.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import net.ferraSolution.food.R
import net.ferraSolution.food.ui.HomeActivity
import timber.log.Timber

/**
 */
class FirebaseCloudMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage)
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {

        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        intent.putExtra("order_created", remoteMessage.data["order_created"])
        Timber.d("${remoteMessage.data["order_created"]}")

        val pendingIntentRandom = System.currentTimeMillis().toInt()
        val notificationRandom = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getActivity(this, pendingIntentRandom, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = "default_notification_channel_id"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.image)
                .setChannelId(channelId)
                .setContentTitle("Ferra food")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
        notificationBuilder.setContentText(remoteMessage.notification?.body)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "default_notification_channel_id"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelId, name, importance)
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(mChannel)
        }
        if (!remoteMessage.notification?.body.isNullOrBlank())
            notificationManager.notify(notificationRandom, notificationBuilder.build())
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        //Replies.setPushNotificationRegistrationToken(s)
        updateServerSideFCMToken(s)

    }

    private fun updateServerSideFCMToken(refreshedToken: String?) {
    }

}