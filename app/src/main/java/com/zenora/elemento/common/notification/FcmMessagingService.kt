package com.zenora.elemento.common.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.zenora.elemento.R
import com.zenora.elemento.common.SharedPreferenceHelper
import com.zenora.elemento.common.constants.AppConstants
import com.zenora.elemento.common.constants.PreferenceConstants
import com.zenora.elemento.common.constants.forceLogoutLiveData
import com.zenora.elemento.feature.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FcmMessagingService : FirebaseMessagingService() {
    private var mNotificationManager: NotificationManager? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val title: String
        val body: String
        if (remoteMessage.data.isNotEmpty()) {
            title = remoteMessage.data[AppConstants.FCM_TITLE] ?: ""
            body = remoteMessage.data[AppConstants.FCM_BODY] ?: ""
        } else {
            title = remoteMessage.notification?.title ?: ""
            body = remoteMessage.notification?.body ?: ""
        }


        val isForceLogout = remoteMessage.data[AppConstants.FCM_KEY] == "ForceLogout"
        if (isForceLogout) {
            forceLogoutLiveData.postValue(remoteMessage.data[AppConstants.FCM_BODY])
        } else {
            if (title.isNotBlank() && body.isNotBlank()) {
                showNotification(title, body, getPendingIntend())
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        SharedPreferenceHelper.saveString(PreferenceConstants.PUSH_TOKEN, token)
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String) {
        Log.d("PushNotification", "New Token : $token")
        //TODO add API call to update token
    }


    private fun showNotification(
        title: String,
        msg: String,
        contentIntent: PendingIntent
    ) {
        mNotificationManager = this
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val bitmap = BitmapFactory.decodeResource(
            this.resources,
            R.mipmap.ic_launcher
        )
        val mBuilder = NotificationCompat.Builder(this, AppConstants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title + "")
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            .setVibrate(longArrayOf(500, 500, 500))
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
            .setLargeIcon(bitmap)
            .setContentText(msg)
        mBuilder.setContentIntent(contentIntent)
        val notificationId = System.currentTimeMillis().toInt()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            val notificationChannel1 = NotificationChannel(
                AppConstants.NOTIFICATION_CHANNEL_ID,
                AppConstants.NOTIFICATION_CHANNEL_ONE,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel1.enableLights(true)
            notificationChannel1.lightColor = Color.RED
            notificationChannel1.enableVibration(true)
            notificationChannel1.vibrationPattern = longArrayOf(500, 500, 500)

            //Channel for different category of notification with different priorities
            val notificationChannel2 = NotificationChannel(
                AppConstants.NOTIFICATION_CHANNEL_ID,
                AppConstants.NOTIFICATION_CHANNEL_TWO,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel2.enableLights(true)
            notificationChannel2.lightColor = Color.GREEN
            notificationChannel2.enableVibration(true)
            notificationChannel2.vibrationPattern = longArrayOf(1000, 1000, 1000)

            assert(mNotificationManager != null)
            mBuilder.setChannelId(AppConstants.NOTIFICATION_CHANNEL_ID)
            mNotificationManager?.createNotificationChannel(notificationChannel1)
        }

        mNotificationManager?.notify(notificationId, mBuilder.build())

    }


    private fun getPendingIntend(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )
    }
}