package com.zenora.elemento.feature.login.repository

import androidx.lifecycle.LiveData
import com.zenora.elemento.common.baseclass.APIResponse
import com.zenora.elemento.feature.login.dataclass.FcmRequest
import com.zenora.elemento.feature.login.dataclass.LoginResponse

interface LoginRepository {
    fun userLoginRepo(email: String, password: String): LiveData<APIResponse<LoginResponse>>
    fun updateFCMToken(fcmRequest: FcmRequest): LiveData<APIResponse<Boolean>>
}