package com.zenora.elemento.common.baseclass

import com.zenora.elemento.BaseApplication
import com.zenora.elemento.R
import com.zenora.elemento.common.network.NoNetworkException
import org.json.JSONObject
import retrofit2.Response

/*
 * Base repository for API call
 */

suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): APIResponse<T> = try {
    val response = call.invoke()
    if (response.isSuccessful && response.body() != null && response.code() == 200) {
        APIResponse.Success(response.body()!!)
    } else {
        val error = response.errorBody()?.toString() ?: ""
        val apiErrorMessage: String = try {
            val errorJson = JSONObject(error)
            errorJson.getJSONArray("messages")[0].toString()
        } catch (e: Exception) {
            BaseApplication.applicationContext().getString(R.string.common_error_message)
        }
        APIResponse.Error(apiErrorMessage)
    }
} catch (e: NoNetworkException) {
    e.printStackTrace()
    APIResponse.Error(e.message)
} catch (e: Exception) {
    e.printStackTrace()
    APIResponse.Error(BaseApplication.applicationContext().getString(R.string.common_error_message))
}


