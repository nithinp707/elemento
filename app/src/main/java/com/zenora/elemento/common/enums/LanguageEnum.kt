package com.zenora.elemento.common.enums

/**
 * This class handles the app languages changes
 */
enum class LanguageEnum(
    /**
     * Return Country Code
     *
     * @return
     */
    val code: String
) {
    DEVICE_LANGUAGE_EN("English"),
    DEVICE_LANGUAGE_AR("عربي"),
    ENGLISH("en"),
    ARABIC("ar"),
    ENGLISH_JSON("app_language-en.json"),
    ARABIC_JSON("app-language-ar.json");

    private var appLanguage: String? = null

    open fun LanguageEnum(appLanguage: String?) {
        this.appLanguage = appLanguage
    }

    open fun getLanguageCode(): String? {
        return appLanguage
    }

}