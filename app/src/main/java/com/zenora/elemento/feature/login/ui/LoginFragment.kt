package com.zenora.elemento.feature.login.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.zenora.elemento.R
import com.zenora.elemento.common.AppEventListener
import com.zenora.elemento.common.AppUtils
import com.zenora.elemento.common.baseclass.APIResponse
import com.zenora.elemento.common.baseclass.BaseFragment
import com.zenora.elemento.common.hideKeyboard
import com.zenora.elemento.databinding.FragmentLoginBinding
import com.zenora.elemento.feature.login.dataclass.LoginResponse
import com.zenora.elemento.feature.login.viewmodel.LoginViewModel
import com.zenora.elemento.feature.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment() {

    private lateinit var navController: NavController
    private val loginViewModel: LoginViewModel by viewModel()
    private lateinit var lBinding: FragmentLoginBinding
    //private val fcmRequest = FcmRequest()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lBinding = FragmentLoginBinding.inflate(inflater)
        return lBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lBinding.lifecycleOwner = this
        lBinding.loginViewModel = loginViewModel
        navController = Navigation.findNavController(view)
        lBinding.editItemUsername.clearFocus()
        lBinding.editItemPassword.clearFocus()

        lBinding.eventListener = object : AppEventListener.LoginEvents {
            override fun showAndHidePassword() {

                if (lBinding.editItemPassword.text.toString().isNotBlank()) {
                    lBinding.buttonShowPassword.isActivated =
                        !lBinding.buttonShowPassword.isActivated
                    if (lBinding.editItemPassword.inputType == (InputType.TYPE_CLASS_TEXT or
                                InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    ) {
                        lBinding.editItemPassword.inputType = InputType.TYPE_CLASS_TEXT
                    } else {
                        lBinding.editItemPassword.inputType = (InputType.TYPE_CLASS_TEXT or
                                InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    }
                    lBinding.editItemPassword.setSelection(lBinding.editItemPassword.text.toString().length)
                }
            }

            override fun forgotPassword() {
                //navController.navigate(LoginFragmentDirection.actionLoginFragmentToForgotPasswordFragment())
            }

            override fun signIn() {
                hideKeyboard()
                if (AppUtils().isEmailValid(lBinding.editItemUsername.text.toString())) {
                    apiLogin()
                } else {
                    lBinding.inputLayoutUsername.helperText =
                        getString(R.string.invalid_email_message)
                    lBinding.editItemUsername.background.clearColorFilter()
                }
            }
        }


        var usernameAvailable = false
        var passwordAvailable = false
        lBinding.editItemUsername.addTextChangedListener {
            lBinding.inputLayoutUsername.helperText = ""
            if (it?.length != 0) {
                usernameAvailable = true
                if (passwordAvailable) {
                    setLoginButtonClickable(true)
                }
            } else {
                setLoginButtonClickable(false)
                usernameAvailable = false
            }
        }

        lBinding.editItemPassword.addTextChangedListener {
            if (it?.length != 0) {
                passwordAvailable = true
                if (usernameAvailable) {
                    setLoginButtonClickable(true)
                }
            } else {
                passwordAvailable = false
                setLoginButtonClickable(false)
            }
        }
    }


    private fun setLoginButtonClickable(isClickable: Boolean) {
        lBinding.buttonSignIn.isEnabled = isClickable
    }

    private fun apiLogin() {
        loginViewModel.apiPerformLogin()?.observe(this, {
            loginAPIResponseManagement(it)
        })
    }

    private fun loginAPIResponseManagement(response: APIResponse<LoginResponse>) {
        when (response) {
            is APIResponse.Loading -> {
                showProgress()
            }
            is APIResponse.Success -> {
                startActivity(Intent(activity, MainActivity::class.java))
                //startBackgroundFcmTokenService()
            }
            is APIResponse.Error -> {
                dismissProgress()
                showSnackBarAlert(response.message)
            }
        }
    }

    /**
     * Start a  background service to update the fcm token to server
     */
    private fun startBackgroundFcmTokenService() {
        try {
            /*FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                fcmRequest.apply {
                    deviceId = getDeviceId()
                    fcmToken = it.token
                }
                loginViewModel.updateFCMToken(fcmRequest).observe(this, Observer { response ->
                    fcmUpdateResponse(response)
                })
            }*/
        } catch (e: Exception) {
            e.fillInStackTrace()
        }
    }

    private fun fcmUpdateResponse(response: APIResponse<Boolean>?) {
        when (response) {
            is APIResponse.Loading -> {
            }
            is APIResponse.Success -> {
                dismissProgress()
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }
            is APIResponse.Error -> {
                dismissProgress()
                showSnackBarAlert(response.message)
            }
        }
    }

}
