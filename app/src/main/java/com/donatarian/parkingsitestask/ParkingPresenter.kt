package com.donatarian.parkingsitestask

import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.donatarian.parkingsitestask.Models.ParkingSite
import com.donatarian.parkingsitestask.Singleton.Utils
import javax.inject.Inject

class ParkingPresenter(var view: ParkingContract.View) : ParkingContract.Presenter {
    var model: ParkingContract.Model

    @Inject
    lateinit var utils: Utils



    init {
        model = ParkingModel()
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