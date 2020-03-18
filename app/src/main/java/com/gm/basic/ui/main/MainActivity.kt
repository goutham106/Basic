package com.gm.basic.ui.main

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.gm.basic.BuildConfig
import com.gm.basic.R
import com.gm.basic.data.Data
import com.gm.basic.location.LocationHelper
import com.gm.basic.location.LocationInterface
import com.gm.basic.room.DataEntity
import com.gm.permission.GmPermissionManager
import com.gm.permission.PermissionListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), Adapter.OnItemClickListener, LocationInterface {

    private val vieModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var adapter: Adapter = Adapter(this)

    private lateinit var locationHelper: LocationHelper
    var permissionManager: GmPermissionManager? = null
    private val snackBarContainer: View
        get() = container//window.decorView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationHelper = LocationHelper(this)

        initRecyclerView()

        initialGPS()

        button_check.setOnClickListener {
            vieModel.checkWebservice(context = this)
        }

        vieModel.data.observe(this, Observer {
            adapter.items = it
        })
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = TopDissolveLayoutManager()//LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }

    override fun onItemClicked(position: Int, data: DataEntity) {
        Toast.makeText(this, data.title, Toast.LENGTH_SHORT).show()
    }


    private fun initialGPS() {

        permissionManager = GmPermissionManager.builder()
            .with(this)
            .requestCode(102)
            .neededPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
            .setPermissionListner(object : PermissionListener {
                override fun onPermissionsGranted(requestCode: Int, acceptedPermission: String) {
                    Toast.makeText(this@MainActivity, "PERMISSION GRANTED", Toast.LENGTH_LONG)
                        .show()
                    startTracking()
                }

                override fun showGrantDialog(grantPermissionTo: String): Boolean {

                    Snackbar.make(
                            snackBarContainer,
                            permissionManager?.getPermissionMessageDialog(grantPermissionTo)
                                .toString(),
                            Snackbar.LENGTH_LONG
                        )
                        .setAction(getString(R.string.action_grant)) { permissionManager?.requestPermissions() }
                        .show()
                    return super.showGrantDialog(grantPermissionTo)
                }

                override fun showRationalDialog(
                    requestCode: Int,
                    deniedPermission: String
                ): Boolean {
                    Snackbar.make(
                            snackBarContainer,
                            permissionManager?.getPermissionMessageDialog(deniedPermission)
                                .toString(),
                            Snackbar.LENGTH_LONG
                        )
                        .setAction(getString(R.string.action_settings)) {
                            GmPermissionManager.gotoSettings(this@MainActivity, PERMISSION_REQUEST)
                        }
                        .show()
                    return super.showRationalDialog(requestCode, deniedPermission)
                }

            })
            .build()

        permissionManager?.requestPermissions()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PERMISSION_REQUEST) {
            permissionManager?.requestPermissions()
        }

    }

    private fun startTracking() {
        locationInterface = this

        if (!locationHelper.isLocationRunning) {

            if (!locationHelper.isNetworkEnabled(this)) {
                locationHelper.startRequestLocation()
            } else {
                Log.e("GPS", "GPS Not Enabled")
            }
        }
    }


    override fun onLocationUpdate(location: Location) {
        Log.e("onLocationUpdate", "LatLang = " + location.latitude + " - " + location.longitude)
    }

    companion object {
        const val PERMISSION_REQUEST = 0
        var locationInterface: LocationInterface? = null
        fun locationUpdate(location: Location) {
            if (locationInterface != null) {
                locationInterface?.onLocationUpdate(location)
            }
        }
    }
}
