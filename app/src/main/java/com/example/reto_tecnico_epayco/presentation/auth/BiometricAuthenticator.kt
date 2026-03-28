package com.example.reto_tecnico_epayco.presentation.auth

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.reto_tecnico_epayco.R

/**
 * Encapsula [BiometricManager] y [BiometricPrompt] sin duplicar lógica en pantallas o ViewModels.
 * El tipo concreto (huella, rostro, etc.) lo decide el sistema según hardware y enrolamiento.
 */
class BiometricAuthenticator(
    private val activity: FragmentActivity
) {

    private val allowedAuthenticators: Int =
        BiometricManager.Authenticators.BIOMETRIC_STRONG or
            BiometricManager.Authenticators.BIOMETRIC_WEAK

    fun authenticate(
        onSuccess: () -> Unit,
        onResult: (BiometricMessage) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)
        val prompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    when (errorCode) {
                        BiometricPrompt.ERROR_USER_CANCELED,
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON,
                        BiometricPrompt.ERROR_CANCELED -> onResult(BiometricMessage.Cancelled)
                        else -> onResult(BiometricMessage.Error(errString.toString()))
                    }
                }

                override fun onAuthenticationFailed() {
                    onResult(BiometricMessage.Failed)
                }
            }
        )
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(activity.getString(R.string.biometric_prompt_title))
            .setSubtitle(activity.getString(R.string.biometric_prompt_subtitle))
            .setNegativeButtonText(activity.getString(R.string.biometric_prompt_negative))
            .setAllowedAuthenticators(allowedAuthenticators)
            .build()
        prompt.authenticate(promptInfo)
    }

    companion object {
        fun statusFor(context: Context): BiometricAuthStatus {
            val allowed =
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.BIOMETRIC_WEAK
            return when (BiometricManager.from(context).canAuthenticate(allowed)) {
                BiometricManager.BIOMETRIC_SUCCESS -> BiometricAuthStatus.Ready
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricAuthStatus.NoHardware
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricAuthStatus.HardwareUnavailable
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAuthStatus.NoneEnrolled
                BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED ->
                    BiometricAuthStatus.SecurityUpdateRequired
                BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> BiometricAuthStatus.Unsupported
                BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> BiometricAuthStatus.Unknown
                else -> BiometricAuthStatus.Unknown
            }
        }
    }
}
