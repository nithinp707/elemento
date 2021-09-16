package com.zenora.elemento.common

interface AppEventListener {

    interface LoginEvents {
        fun showAndHidePassword()
        fun forgotPassword()
        fun signIn()
    }
}