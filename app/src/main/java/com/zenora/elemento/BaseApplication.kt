package com.zenora.elemento

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.zenora.elemento.common.koincomponents.networkModule
import com.zenora.elemento.common.koincomponents.repositoryModule
import com.zenora.elemento.common.koincomponents.viewModelModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@BaseApplication)
            modules(listOf(networkModule, viewModelModule, repositoryModule))
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        INSTANCE = this
    }

    companion object {
        private var INSTANCE: BaseApplication? = null

        fun applicationContext(): Context {
            return INSTANCE?.applicationContext!!
        }
    }
}