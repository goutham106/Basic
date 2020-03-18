package com.gm.basic.ui.biometric

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.gm.basic.R
import java.util.concurrent.Executors.newSingleThreadExecutor

class BiometricActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric)

        val executor = newSingleThreadExecutor()
        val activity: FragmentActivity = this // reference to activity
        val biometricManager = BiometricManager.from(this)
        canAuthenticate(biometricManager)
        val biometricPrompt =
            BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        // user clicked negative button
                        showToast("Button Clicked")
                    } else {
                        showToast(errString.toString())
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    showToast("Success $result")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showToast("Failed")
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Set the title to display.")
            .setSubtitle("Set the subtitle to display.")
            .setDescription("Set the description to display")
            .setNegativeButtonText("Negative Button")
            .setConfirmationRequired(false)
//            .setDeviceCredentialAllowed(true)
            .build()


        findViewById<Button>(R.id.authenticateButton).setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

        findViewById<Button>(R.id.faceAvailableButton).setOnClickListener {
            if (hasBiometrics(it.context)) {
                Toast.makeText(this, "Face Detection Available", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Face Detection Not Available", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showToast(message: String) {
        this.runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun canAuthenticate(biometricManager: BiometricManager) {
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")

            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Log.e(
                    "MY_APP_TAG", "The user hasn't associated " +
                            "any biometric credentials with their account."
                )
        }

    }

    fun hasBiometrics(context: Context): Boolean {
        val pm = context.packageManager
        return (
                //pm.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
                //|| pm.hasSystemFeature(PackageManager.FEATURE_IRIS) ||
                pm.hasSystemFeature(PackageManager.FEATURE_FACE)
                )
    }
}
