package com.zenora.elemento.common

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Activity.showKeyboard() {
    (this.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
        View(this), 0
    )
}

fun Activity.hideKeyboard() {
    (this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        View(this).windowToken, 0
    )
}

fun Fragment.showKeyboard() {
    (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
        view, 0
    )
}

fun Fragment.hideKeyboard() {
    (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
        view?.windowToken,
        0
    )
}