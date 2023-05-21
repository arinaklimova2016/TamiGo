package com.tamigo.main

import android.app.Application
import com.tamigo.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TamiGoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TamiGoApplication)
            modules(appModules)
        }
    }
}
