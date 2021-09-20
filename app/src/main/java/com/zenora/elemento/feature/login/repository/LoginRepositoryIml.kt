package com.zenora.elemento.feature.login.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.zenora.elemento.common.baseclass.APIResponse
import com.zenora.elemento.common.baseclass.safeApiCall
import com.zenora.elemento.common.network.APIInterfaces
import com.zenora.elemento.feature.login.dataclass.FcmRequest
import com.zenora.elemento.feature.login.dataclass.LoginRequest
import com.zenora.elemento.feature.login.dataclass.LoginResponse

class LoginRepositoryIml(private var projectAPIInterface: APIInterfaces) : LoginRepository {
    override fun userLoginRepo(
        email: String,
        password: String
    ): LiveData<APIResponse<LoginResponse>> {
        return liveData {
            emit(APIResponse.Loading())
            emit(safeApiCall {
                projectAPIInterface.userLogin(LoginRequest(email, password))
            })
        }
    }

    override fun updateFCMToken(fcmRequest: FcmRequest): LiveData<APIResponse<Boolean>> {
        return liveData {
            emit(APIResponse.Loading())
            emit(safeApiCall {
                projectAPIInterface.updateFcmToken(fcmRequest)
            })
        }
    }

} 