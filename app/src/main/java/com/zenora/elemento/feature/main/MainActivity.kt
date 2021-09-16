package com.zenora.elemento.feature.main

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.zenora.elemento.R
import com.zenora.elemento.common.SharedPreferenceHelper
import com.zenora.elemento.common.baseclass.BaseActivity
import com.zenora.elemento.common.constants.PreferenceConstants
import com.zenora.elemento.databinding.ActivityMainBinding
import com.zenora.elemento.feature.login.ui.LoginActivity

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    @Suppress("UselessCallOnNotNull")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val loginResponseData =
            SharedPreferenceHelper.getLoginResponse(PreferenceConstants.LOGIN_RESPONSE)
        if (loginResponseData == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else if (loginResponseData.accessToken.isNullOrEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}