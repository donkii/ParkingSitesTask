package com.donatarian.parkingsitestask

import android.app.Application
import android.content.Context
import com.donatarian.parkingsitestask.dagger.AppComponent
import com.donatarian.parkingsitestask.dagger.DaggerAppComponent
import com.donatarian.parkingsitestask.dagger.UtilsModule

class App : Application() {



    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().utilsModule(UtilsModule(applicationContext)).build()
    }

    companion object {
        lateinit var appComponent: AppComponent

    }

}