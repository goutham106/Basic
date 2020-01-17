package com.gm.basic.location

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task


/**
 * Created by Gowtham on 07/03/18.
 */
class LocationHelper(con: Context) {
    private var ctx: Context = con
    private lateinit var mLocationRequest: LocationRequest
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    var isLocationRunning: Boolean = false
    lateinit var result: Task<LocationSettingsResponse>

    companion object {
        const val REQUEST_CHECK_SETTINGS = 0x1

        private const val UPDATE_INTERVAL = (1000 * 10 * 1).toLong()
        private const val FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2
        private const val MAX_WAIT_TIME = (UPDATE_INTERVAL * 3) / 2
        private const val DISPLACEMENT = 5f // meters
    }

    init {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)
        createLocationRequest()
    }

//    constructor(con: Context, listner: BaseInterface) : this(con) {
//        baseInterface = listner
//    }

    @SuppressLint("RestrictedApi")
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest(

        )

        /**
         * Sets the desired interval for active location updates. This interval is
         * inexact. You may not receive updates at all if no location sources are available, or
         * you may receive them slower than requested. You may also receive updates faster than
         * requested if other applications are requesting location at a faster interval.
         * Note: apps running on "O" devices (regardless of targetSdkVersion) may receive updates
         * less frequently than this interval when the app is no longer in the foreground.
         */
        mLocationRequest.interval = UPDATE_INTERVAL

        /**
         * Sets the fastest rate for active location updates. This interval is exact, and your
         * application will never receive updates faster than this value.
         */
        mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL

        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        /**
         * Sets the maximum time when batched location updates are delivered. Updates may be
         * delivered sooner than this interval.
         */
        mLocationRequest.maxWaitTime = MAX_WAIT_TIME


        mLocationRequest.smallestDisplacement = DISPLACEMENT

    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(ctx, LocationUpdatesBroadcastReceiver::class.java)
        intent.action = LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES
        return PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun startRequestLocation() {
        try {
            Log.e("startRequestLocation", "Starting location updates")
            isLocationRunning = true
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest, getPendingIntent())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun stopRequestLocation() {
        Log.e("stopRequestLocation", "Removing location updates")
        isLocationRunning = false
        mFusedLocationClient?.removeLocationUpdates(getPendingIntent())
    }

    @SuppressLint("MissingPermission")
    fun isNetworkEnabled(activity: Activity): Boolean {
        var isNetworkEnabled = false
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()

        result = LocationServices.getSettingsClient(ctx).checkLocationSettings(locationSettingsRequest)

        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                Log.e("startRequestLocation", "Starting location updates")
                isLocationRunning = true
                mFusedLocationClient?.requestLocationUpdates(mLocationRequest, getPendingIntent())

                isNetworkEnabled = true
            } catch (exception: ApiException) {

                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                        isNetworkEnabled = try {
                            val resolvable = exception as ResolvableApiException
                            resolvable.startResolutionForResult(
                                    activity,
                                    REQUEST_CHECK_SETTINGS
                            )
                            false
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                            false
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                            false
                        }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }


        return isNetworkEnabled
    }

}