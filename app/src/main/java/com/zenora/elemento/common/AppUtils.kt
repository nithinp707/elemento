@file:Suppress("unused")

package com.zenora.elemento.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.webkit.URLUtil
import com.zenora.elemento.BaseApplication
import com.zenora.elemento.R
import com.google.gson.Gson
import java.io.IOException
import java.lang.reflect.Type
import java.net.URL


/**
 * This class manages the App Utilities
 */
class AppUtils {

    companion object {
        private const val TAG = "AppUtils"
        private var appUtils: AppUtils? = null
    }

    /**
     * Function used to Check If URL Is Valid
     *
     * @param url : web url
     * @return : weather url valid or not
     */
    fun isValidUrl(url: String?): Boolean {
        return URLUtil.isValidUrl(url)
    }

    fun maskEmailAddress(email: String): String {
        val mask = "****"
        val at = email.indexOf("@")
        if (at > 2) {
            val maskLen = (at / 2).coerceAtLeast(2).coerceAtMost(4)
            val start = (at - maskLen) / 2
            return email.substring(0, start) + mask.substring(
                0,
                maskLen
            ) + email.substring(start + maskLen)
        }
        return email
    }

    fun maskPhoneNumber(phone: String): String {
        val mask = "****"
        val start = 2
        val end = phone.length - 2
        return phone.substring(0, start) + mask + phone.substring(end)
    }

    /**
     * Function for fetching instance
     */
    val instance: AppUtils?
        get() = if (appUtils == null) {
            appUtils = AppUtils()
            appUtils
        } else appUtils

    /**
     * Fetch Unique Device ID
     */
    @SuppressLint("HardwareIds")
    fun getUniqueDeviceID(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     * Function for Fetching Device Information
     *
     * @return : device information
     */
    val deviceInformation: String
        get() = String.format("Device: %s OS Version: %s", Build.MODEL, Build.VERSION.RELEASE)

    /**
     * Function for Fetching & Displaying App Version
     *
     * @param mContext : context
     * @return : app version
     */
    fun fetchAppVersion(mContext: Context): String {
        var appVersionName = ""
        try {
            val pInfo: PackageInfo =
                mContext.packageManager.getPackageInfo(mContext.packageName, 0)
            appVersionName = mContext.getString(R.string.version) + " " + pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            if (e.message != null) Log.e(TAG, e.message ?: "null")
        }
        return appVersionName
    }

    /**
     * Json Object to String Converter
     */
    fun jsonToString(`object`: Any?): String {
        return Gson().toJson(`object`)
    }

    /**
     * String to Json Object Converter
     */
    fun <T> stringToJson(dataType: Type?, prefKey: String?): T? {
        if (prefKey != null && prefKey.isNotEmpty() && dataType != null) {
            val json: String = SharedPreferenceHelper.getString(prefKey)!!
            return Gson().fromJson(json, dataType)
        }
        return null
    }

    /**
     * Function for Converting URL String To Bitmap
     *
     * @param imageURL : image url string
     * @return : Bitmap
     */
    fun urlToBitmap(imageURL: String?): Bitmap? {
        try {
            val url = URL(imageURL)
            return BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: IOException) {
            Log.e(TAG, e.message ?: "null")
        }
        return null
    }

    /**
     * Function for Getting First Letter From String
     *
     * @param text : string variable
     * @return : first character of word
     */
    fun getFirstLetter(text: String?): String {
        return if (text != null && text.isNotEmpty()) text.substring(0, 1) else ""
    }

    /**
     * Function for Removing Leading Zero's
     *
     * @param phoneNumber : phone number
     * @return : 0 removed phone number
     */
    fun removeLeadingZeros(phoneNumber: String?): String {
        return if (phoneNumber != null && phoneNumber.isNotEmpty()) phoneNumber
            .replaceFirst("^0+(?!$)".toRegex(), "") else ""
    }

    /**
     * Function for Checking If Valid Email Address
     *
     * @param target : email address
     * @return : weather email valid or not
     */
    fun isEmailValid(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target ?: "").matches()
    }

    /**
     * Returns the consumer friendly device name
     */
    val deviceName: String
        get() {
            val manufacturer: String = Build.MANUFACTURER
            val model: String = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                capitalize(model)
            } else capitalize(manufacturer) + " " + model
        }

    private fun capitalize(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true
        val phrase = StringBuilder()
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c))
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase.append(c)
        }
        return phrase.toString()
    }
}

fun isInternetConnected(): Boolean {
    return with(
        BaseApplication.applicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
    ) {
        isConnected(activeNetworkInfo)
    }
}

fun isConnected(network: NetworkInfo?): Boolean {
    return when (network) {
        null -> false
        else -> with(network) { isConnected && (type == TYPE_WIFI || type == TYPE_MOBILE) }
    }
}

fun isConnected(networkCapabilities: NetworkCapabilities?): Boolean {
    return when (networkCapabilities) {
        null -> false
        else -> with(networkCapabilities) {
            hasTransport(TRANSPORT_CELLULAR) || hasTransport(
                TRANSPORT_WIFI
            )
        }
    }
}