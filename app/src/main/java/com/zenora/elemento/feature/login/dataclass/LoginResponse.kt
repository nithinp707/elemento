package com.zenora.elemento.feature.login.dataclass


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("accessToken")
    var accessToken: String,
    @SerializedName("isClockedIn")
    var isClockedIn: Boolean,
    @SerializedName("refreshToken")
    var refreshToken: String,
    @SerializedName("role")
    var role: String,
    @SerializedName("userId")
    var userId: String
)

data class LoginRequest(var emailId: String, var password: String)


data class LogoutRequest(
    @SerializedName("refreshToken")
    var refreshToken: String,
    @SerializedName("deviceId")
    var deviceId: String
)

data class LogoutResponse(
    @SerializedName("status")
    var status: Boolean
)




