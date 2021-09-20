package com.zenora.elemento.common.constants

import androidx.lifecycle.MutableLiveData

/**
 * This class contains the constants specific to the application
 */

object AppConstants {
    const val ACTION_LOGOUT = "com.zenora.elemento.action.LOGOUT"
    const val ROOT_DIRECTORY_NAME = "Demo"
    const val TEMP_DIRECTORY = "Temp"

    const val SECONDARY_STORAGE = "SECONDARY_STORAGE"
    const val EXTERNAL_STORAGE = "EXTERNAL_STORAGE"
    const val DIRECTORY_WHATSAPP = "whatsapp"
    const val EXTERNAL_STORAGE_DOCUMENT = "com.android.externalstorage.documents"
    const val DOWNLOAD_DOCUMENT = "com.android.providers.downloads.documents"
    const val MEDIA_DOCUMENT = "com.android.providers.media.documents"
    const val GOOGLE_PHOTO_URI = "com.google.android.apps.photos.content"
    const val WHATSAPP_MEDIA = "com.whatsapp.provider.media"
    const val GOOGLE_DRIVE_URI = "com.google.android.apps.docs.storage"
    const val GOOGLE_DRIVE_URI_LEGACY = "com.google.android.apps.docs.storage.legacy"

    /*App Orientations*/
    const val APP_ORIENTATION_90 = 90
    const val APP_ORIENTATION_180 = 180
    const val APP_ORIENTATION_270 = 270

    //Push notification constants
    const val FCM_TITLE = "title"
    const val FCM_BODY = "body"
    const val FCM_KEY = "key"
    const val NOTIFICATION_CHANNEL_ID = "100201101"
    const val NOTIFICATION_CHANNEL_ONE = "One"
    const val NOTIFICATION_CHANNEL_TWO = "Two"
}

var forceLogoutLiveData = MutableLiveData<String>()
