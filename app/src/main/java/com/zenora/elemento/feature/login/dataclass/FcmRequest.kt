package com.zenora.elemento.feature.login.dataclass

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Class for post fcm request to server
 */
data class FcmRequest(
    @SerializedName("fcmToken")
    @Expose
    var fcmToken: String? = null,
    @SerializedName("deviceId")
    var deviceId: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(fcmToken)
        writeString(deviceId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FcmRequest> = object : Parcelable.Creator<FcmRequest> {
            override fun createFromParcel(source: Parcel): FcmRequest = FcmRequest(source)
            override fun newArray(size: Int): Array<FcmRequest?> = arrayOfNulls(size)
        }
    }
}