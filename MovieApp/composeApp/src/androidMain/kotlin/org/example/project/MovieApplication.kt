package org.example.project

import android.app.Application
import di.initKoin
import org.koin.android.ext.koin.androidContext

internal class MovieApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MovieApplication)
        }
    }
}
