package com.donatarian.parkingsitestask.dagger

import com.donatarian.parkingsitestask.MapsActivity
import com.donatarian.parkingsitestask.presenters.ParkingPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [UtilsModule::class])
interface AppComponent {
   fun inject(mapsActivity: MapsActivity)
   fun inject(parkingPresenter: ParkingPresenter)
}