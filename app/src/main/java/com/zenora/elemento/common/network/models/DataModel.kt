package com.zenora.elemento.common.network.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * This model class is used as a base network model for all Retrofit calls
 */

data class DataModel<T>
/**
 * Returns API Request Body
 *
 * @param code
 * @param message
 * @param data
 */(

    //The server response code.
    @field:SerializedName("code") var code: Int,

    //The server response message.
    @field:SerializedName("message") var message: String,

    //The server response message.
    @field:SerializedName("data") var data: T

) : Serializable