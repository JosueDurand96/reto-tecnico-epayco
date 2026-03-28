package com.example.reto_tecnico_epayco.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.reto_tecnico_epayco.R
import com.example.reto_tecnico_epayco.presentation.auth.BiometricAuthStatus
import com.example.reto_tecnico_epayco.presentation.auth.BiometricAuthenticator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) : ViewModel() {

    private val _biometricStatus =
        MutableStateFlow(BiometricAuthenticator.statusFor(applicationContext))
    val biometricStatus: StateFlow<BiometricAuthStatus> = _biometricStatus.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    fun refreshBiometricAvailability() {
        _biometricStatus.value = BiometricAuthenticator.statusFor(applicationContext)
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun validateTraditionalLogin(username: String, password: String): Boolean {
        return if (username.isBlank() || password.isBlank()) {
            _userMessage.value = applicationContext.getString(R.string.login_error_empty_fields)
            false
        } else {
            true
        }
    }

    fun showMessage(message: String) {
        _userMessage.update { message }
    }
}
