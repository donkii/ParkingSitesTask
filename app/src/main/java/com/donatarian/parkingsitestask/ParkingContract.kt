package com.donatarian.parkingsitestask

import com.donatarian.parkingsitestask.Models.ParkingSite

interface ParkingContract {

    interface Model{

    }

    interface View{
        fun showList(list: List<ParkingSite>)
    }

    interface Presenter{
        fun loadList()
    }
}