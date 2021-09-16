package com.zenora.elemento.feature.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zenora.elemento.BaseApplication
import com.zenora.elemento.R
import com.zenora.elemento.common.SharedPreferenceHelper
import com.zenora.elemento.common.baseclass.APIResponse
import com.zenora.elemento.common.baseclass.ObservableViewModel
import com.zenora.elemento.common.constants.PreferenceConstants
import com.zenora.elemento.feature.login.dataclass.FcmRequest
import com.zenora.elemento.feature.login.dataclass.LoginResponse
import com.zenora.elemento.feature.login.repository.LoginRepository
import com.google.gson.Gson

class LoginViewModel(private var loginRepository: LoginRepository) : ObservableViewModel() {

    var userEmail: String = ""
    var password: String = ""

    var snackBarMessage: MutableLiveData<String> = MutableLiveData()


    fun apiPerformLogin(): LiveData<APIResponse<LoginResponse>>? {
        return takeIf { isInputFieldValid() }?.getRepositoryItem()
    }

    private fun getRepositoryItem(): LiveData<APIResponse<LoginResponse>> {
        val mediatorLiveData = MutableLiveData<APIResponse<LoginResponse>>()
        return Transformations.switchMap(loginRepository.userLoginRepo(userEmail, password)) {
            if (it is APIResponse.Success) {
                SharedPreferenceHelper.saveString(
                    PreferenceConstants.ACCESS_TOKEN,
                    it.data.accessToken
                )
                SharedPreferenceHelper.saveString(
                    PreferenceConstants.REFRESH_TOKEN,
                    it.data.refreshToken
                )
                SharedPreferenceHelper.saveLoginResponse(
                    PreferenceConstants.LOGIN_RESPONSE,
                    Gson().toJson(it.data)
                )
            }
            mediatorLiveData.value = it
            mediatorLiveData
        }
    }


    private fun isInputFieldValid(): Boolean {
        if (isFieldEmpty()) return false

        return true
    }

    private fun isFieldEmpty(): Boolean {
        if (userEmail.isEmpty()) {
            snackBarMessage.value =
                BaseApplication.applicationContext().getString(R.string.email_empty)
            return true
        }
        if (password.isEmpty()) {
            snackBarMessage.value =
                BaseApplication.applicationContext().getString(R.string.pass_empty)
            return true
        }
        return false
    }

    fun updateFCMToken(fcmRequest: FcmRequest) = loginRepository.updateFCMToken(fcmRequest)

}