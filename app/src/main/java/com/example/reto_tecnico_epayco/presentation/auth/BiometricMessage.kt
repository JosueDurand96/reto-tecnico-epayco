package com.example.reto_tecnico_epayco.presentation.auth

/**
 * Resultado de un intento de [androidx.biometric.BiometricPrompt] para mensajes en UI.
 */
sealed interface BiometricMessage {
    data object Cancelled : BiometricMessage
    data class Error(val message: String) : BiometricMessage
    data object Failed : BiometricMessage
}
