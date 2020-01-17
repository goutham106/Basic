package com.gm.basic.location

import android.location.Location

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 2020-01-17.
 */
interface LocationInterface {
    fun onLocationUpdate(location: Location)
}