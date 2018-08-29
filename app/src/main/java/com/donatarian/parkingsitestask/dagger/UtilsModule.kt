package com.donatarian.parkingsitestask.dagger

import android.content.Context
import com.donatarian.parkingsitestask.Singleton.Utils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilsModule(var context: Context) {

    @Singleton
    @Provides
    fun providesUtils(): Utils {
        return Utils(context)
    }



}