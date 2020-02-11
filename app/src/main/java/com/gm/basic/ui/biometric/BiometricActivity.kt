package com.gm.basic.ui.biometric

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        val biometricPrompt =
            BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        // user clicked negative button
                        activity.runOnUiThread {
                            Toast.makeText(activity, "Button Clicked", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        activity.runOnUiThread {
                            Toast.makeText(activity, errString, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    activity.runOnUiThread {
                        Toast.makeText(activity, "Success $result", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    activity.runOnUiThread {
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Set the title to display.")
            .setSubtitle("Set the subtitle to display.")
            .setDescription("Set the description to display")
            //.setNegativeButtonText("Negative Button")
            .setConfirmationRequired(false)
            .setDeviceCredentialAllowed(true)
            .build()

        findViewById<Button>(R.id.authenticateButton).setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }
}
