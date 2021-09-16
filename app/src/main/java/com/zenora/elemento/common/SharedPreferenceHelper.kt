@file:Suppress("unused")

package com.zenora.elemento.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.zenora.elemento.BaseApplication
import com.zenora.elemento.feature.login.dataclass.LoginResponse
import com.google.gson.Gson

/**
 * This Object manages the Preference Storage
 * Save & Retrieve Values from the Preference Storage
 * Supported OffersDataModel Types --> String, Integer, Float, Long, Boolean
 * Clear all Preference
 * Clear a particular preference value
 */
@SuppressLint("ApplySharedPref")
object SharedPreferenceHelper {

    private fun getSharedPreference(): SharedPreferences {
        val context = BaseApplication.applicationContext()
        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }


    /**
     * Function for Saving Integer value to Preference Storage
     */
    fun saveString(key: String, value: String?) {
        getSharedPreference().edit().putString(key, value).commit()
    }


    /**
     * Function for Retrieving String value from Preference Storage
     */
    fun getString(key: String): String? {
        return getSharedPreference().getString(key, "")
    }


    /**
     * Function for Saving Integer value to Preference Storage
     */
    fun saveInt(key: String, value: Int) {
        getSharedPreference().edit().putInt(key, value).commit()
    }


    /**
     * Function for Retrieving Integer value from Preference Storage
     */
    fun getInt(key: String): Int {
        return getSharedPreference().getInt(key, 0)
    }


    /**
     * Function for Saving Float value to Preference Storage
     */
    fun saveFloat(key: String, value: Float) {
        getSharedPreference().edit().putFloat(key, value).commit()
    }


    /**
     * Function for Retrieving Float value from Preference Storage
     */
    fun getFloat(key: String): Float {
        return getSharedPreference().getFloat(key, 0f)
    }


    /**
     * Function for Saving Long value to Preference Storage
     */
    fun saveLong(key: String, value: Long) {
        getSharedPreference().edit().putLong(key, value).commit()
    }


    /**
     * Function for Retrieving Long value from Preference Storage
     */
    fun getLong(key: String): Long {
        return getSharedPreference().getLong(key, 0L)
    }


    /**
     * Function for Saving Boolean value to Preference Storage
     */
    fun saveBoolean(key: String, value: Boolean) {
        getSharedPreference().edit().putBoolean(key, value).commit()
    }


    /**
     * Function for Retrieving Boolean value from Preference Storage
     */
    fun getBoolean(key: String): Boolean {
        return getSharedPreference().getBoolean(key, false)
    }


    /**
     * Function for saving the user details after login
     *
     * @param loginResponse : user login details in string format
     * @param key : user login data saved key
     * @return
     */
    fun saveLoginResponse(loginResponse: String, key: String) {
        getSharedPreference().edit().putString(key, loginResponse).commit()
    }


    /**
     * Function for getting the saved user login details
     *
     * @param key : user login data saved key
     * @return : saved user details
     */
    fun getLoginResponse(key: String) =
        Gson().fromJson(getSharedPreference().getString(key, ""),
            LoginResponse::class.java) ?: null


    /**
     * Function for Checking if Preference Store Contains any Value based on the Key
     */
    fun isPreferenceExist(key: String): Boolean {
        return getSharedPreference().contains(key)
    }


    /**
     * Function for Removing a Particular Shared Preference Based on The Key
     */
    fun removePreference(key: String) {
        getSharedPreference().edit().remove(key).commit()
    }


    /**
     * Function for Clearing All Shared Preference
     */
    fun clearAllSharedPreferences() {
        getSharedPreference().edit().clear().commit()
    }
}