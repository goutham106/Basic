package com.gm.basic.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by Gowtham on 07/03/18.
 * Copyright Indyzen Inc, 2018.
 */
class LocationUpdatesBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_PROCESS_UPDATES: String = "com.skyhop.driver.location.action.PROCESS_UPDATES"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (ACTION_PROCESS_UPDATES == intent?.action) {
            val result = LocationResult.extractResult(intent)
            if (result != null) {
//                val locations = result.locations
                val location = result.lastLocation
//                val location = locations.last()

                BaseActivity.locationUpdate(location)
                BaseFragment.locationUpdate(location)
            }
        }
    }
}