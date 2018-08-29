package com.donatarian.parkingsitestask.dagger

import com.donatarian.parkingsitestask.MapsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [UtilsModule::class])
interface AppComponent {
   fun inject(mapsActivity: MapsActivity)
}