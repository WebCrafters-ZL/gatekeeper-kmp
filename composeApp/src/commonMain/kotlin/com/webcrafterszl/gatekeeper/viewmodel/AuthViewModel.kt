package com.webcrafterszl.gatekeeper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webcrafterszl.gatekeeper.data.model.LoginRequest
import com.webcrafterszl.gatekeeper.data.model.Role
import com.webcrafterszl.gatekeeper.data.remote.AuthRepository
import com.webcrafterszl.gatekeeper.util.JwtUtils
import com.webcrafterszl.gatekeeper.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Define os possíveis estados da UI para a tela de login.
 */
sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Success(val role: Role) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

/**
 * ViewModel para a tela de Login.
 */
class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState = _loginUiState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading
            try {
                val loginRequest = LoginRequest(_email.value, _password.value)
                val authResponse = authRepository.login(loginRequest)

                // 1. Salva o token no SessionManager para uso futuro.
                SessionManager.token = authResponse.token
                
                // 2. Decodifica o token para obter o perfil.
                val userRole = JwtUtils.decodeRole(authResponse.token)

                if (userRole != null) {
                    _loginUiState.value = LoginUiState.Success(userRole)
                } else {
                    SessionManager.token = null // Limpa o token se a decodificação falhar.
                    _loginUiState.value = LoginUiState.Error("Token inválido ou perfil não reconhecido.")
                }
                
            } catch (e: Exception) {
                SessionManager.token = null // Limpa o token em caso de erro de rede.
                _loginUiState.value = LoginUiState.Error(e.message ?: "Ocorreu um erro desconhecido.")
            }
        }
    }
    
    fun resetState() {
        _loginUiState.value = LoginUiState.Idle
    }
}