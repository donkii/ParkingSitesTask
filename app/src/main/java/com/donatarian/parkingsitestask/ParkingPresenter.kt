package com.donatarian.parkingsitestask

import android.os.Handler
import com.donatarian.parkingsitestask.Singleton.Utils
import javax.inject.Inject

class ParkingPresenter(var view: ParkingContract.View) : ParkingContract.Presenter {

    @Inject
    lateinit var utils: Utils

    init {
        App.appComponent.inject(this)
    }

    override fun loadList() {
        utils.getDataFromAPI(Handler {

            if (it.what == 1) {
                view.showList(utils.parkingSiteList)
            }
            true
        })
    }
}