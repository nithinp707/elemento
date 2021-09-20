package com.zenora.elemento.common.network


import com.google.gson.annotations.SerializedName

data class RefreshTokenResponseModel(
    @SerializedName("refreshToken")
    var refreshToken: String,
    @SerializedName("token")
    var token: String
)