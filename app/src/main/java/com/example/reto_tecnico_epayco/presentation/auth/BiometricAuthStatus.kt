package com.example.reto_tecnico_epayco.presentation.auth

/**
 * Estado de disponibilidad de biometría según [androidx.biometric.BiometricManager.canAuthenticate].
 */
sealed interface BiometricAuthStatus {
    data object Ready : BiometricAuthStatus
    data object NoHardware : BiometricAuthStatus
    data object HardwareUnavailable : BiometricAuthStatus
    data object NoneEnrolled : BiometricAuthStatus
    data object SecurityUpdateRequired : BiometricAuthStatus
    data object Unsupported : BiometricAuthStatus
    data object Unknown : BiometricAuthStatus
}
