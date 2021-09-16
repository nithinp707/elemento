package com.zenora.elemento.common

import android.content.Context
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.zenora.elemento.common.SharedPreferenceHelper.getString
import com.zenora.elemento.common.constants.PreferenceConstants
import com.zenora.elemento.common.enums.LanguageEnum
import com.google.android.material.textfield.MaterialAutoCompleteTextView

/**
 * This class manages Application Fonts & Text Sizes
 */
object FontFactory {
    private const val TAG = "FontFactory"

    /*Font Styles*/
    const val REGULAR = "REGULAR"
    const val MEDIUM = "MEDIUM"
    const val BOLD = "BOLD"

    /*English Font Types*/
    private const val ENGLISH_REGULAR = "fonts/Cairo-Regular.ttf"
    private const val ENGLISH_MEDIUM = "fonts/Cairo-SemiBold.ttf"
    private const val ENGLISH_BOLD = "fonts/Cairo-Bold.ttf"

    /*Arabic Font Types*/
    private const val ARABIC_REGULAR = "fonts/Cairo-Regular.ttf"
    private const val ARABIC_MEDIUM = "fonts/Cairo-SemiBold.ttf"
    private const val ARABIC_BOLD = "fonts/Cairo-Bold.ttf"

    /**
     * Function for Setting Font Style & Text Size
     *
     * @param context
     * @param view
     * @param textStyle
     * @param textSize
     */
    fun setTypeface(context: Context, view: View?, textStyle: String, textSize: Int) {
        try {
            when (view) {
                is ViewGroup -> {
                    for (i in 0 until view.childCount) {
                        val child = view.getChildAt(i)
                        setTypeface(context, child, textStyle, textSize)
                    }
                }
                is CheckBox -> {
                    view.textSize = processTextSize(context, textSize)
                    view.typeface = processFont(context, textStyle)
                }
                is MaterialAutoCompleteTextView -> {
                    view.textSize = processTextSize(context, textSize)
                    view.typeface = processFont(context, textStyle)
                }
                is EditText -> {
                    view.textSize = processTextSize(context, textSize)
                    view.typeface = processFont(context, textStyle)
                }
                is Button -> {
                    view.textSize = processTextSize(context, textSize)
                    view.typeface = processFont(context, textStyle)
                }
                is TextView -> {
                    view.textSize = processTextSize(context, textSize)
                    view.typeface = processFont(context, textStyle)
                }
            }
        } catch (e: Exception) {
            if (e.message != null) Log.e(TAG, "Exception" + e.message)
        }
    }

    /**
     * Function for Processing The Font Styles
     *
     * @param context
     * @param textStyle
     * @return
     */
    private fun processFont(context: Context, textStyle: String): Typeface {
        if (getString(PreferenceConstants.APP_LANGUAGE)
            == LanguageEnum.ENGLISH.getLanguageCode()
        ) {
            when (textStyle) {
                REGULAR -> return Typeface.createFromAsset(context.assets, ENGLISH_REGULAR)
                MEDIUM -> return Typeface.createFromAsset(context.assets, ENGLISH_MEDIUM)
                BOLD -> return Typeface.createFromAsset(context.assets, ENGLISH_BOLD)
                else -> Log.d(TAG, "Unexpected value: $textStyle")
            }
        } else if (getString(PreferenceConstants.APP_LANGUAGE)
            == LanguageEnum.ARABIC.getLanguageCode()
        ) {
            when (textStyle) {
                REGULAR -> return Typeface.createFromAsset(context.assets, ARABIC_REGULAR)
                MEDIUM -> return Typeface.createFromAsset(context.assets, ARABIC_MEDIUM)
                BOLD -> return Typeface.createFromAsset(context.assets, ARABIC_BOLD)
                else -> Log.d(TAG, "Unexpected value: $textStyle")
            }
        }
        return Typeface.createFromAsset(context.assets, ENGLISH_REGULAR)
    }

    /**
     * Function for Processing Text Size
     *
     * @param context
     * @param textSize
     * @return
     */
    private fun processTextSize(context: Context, textSize: Int): Float {
        val valueInPixels = context.resources.getDimension(textSize)
        val dp = convertPixelsToDp(valueInPixels, context)
        if (getString(PreferenceConstants.APP_LANGUAGE)
                .equals(LanguageEnum.ENGLISH.getLanguageCode())
        ) return dp else if (getString(PreferenceConstants.APP_LANGUAGE)
                .equals(LanguageEnum.ARABIC.getLanguageCode())
        ) return dp + 1
        return dp
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    private fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}
