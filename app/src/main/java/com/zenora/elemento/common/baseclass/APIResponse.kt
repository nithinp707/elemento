package com.zenora.elemento.common.baseclass

/**
 * Base class format for All API response
 */

sealed class APIResponse<out T : Any> {
    data class Success<T : Any>(val data: T) : APIResponse<T>()
    data class Error(val message: String) : APIResponse<Nothing>()
    data class Loading(val str: String = "") : APIResponse<Nothing>()
}