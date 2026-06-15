package com.webcrafterszl.gatekeeper.viewmodel

import com.webcrafterszl.gatekeeper.data.model.LoginRequest
import com.webcrafterszl.gatekeeper.data.remote.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

/**
 * Define os possíveis estados da UI para a tela de login.
 * Usar uma sealed interface é uma prática moderna que garante um tratamento de estados
 * exaustivo e seguro no bloco `when` do Compose.
 */
sealed interface LoginUiState {
    object Idle : LoginUiState // Estado inicial, nada aconteceu ainda.
    object Loading : LoginUiState // Carregando, aguardando a resposta da rede.
    data class Success(val token: String) : LoginUiState // Sucesso, contém o token de autenticação.
    data class Error(val message: String) : LoginUiState // Erro, contém a mensagem a ser exibida.
}

/**
 * ViewModel para a tela de Login.
 * Responsável por gerenciar o estado da UI e se comunicar com o AuthRepository.
 */
class AuthViewModel(
    // Injetamos o repositório para manter a separação de responsabilidades e facilitar testes.
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    // Fluxo de estado para o campo de e-mail, observável pela UI.
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    // Fluxo de estado para o campo de senha.
    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    // Fluxo de estado para o estado geral da UI (Idle, Loading, Success, Error).
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState = _loginUiState.asStateFlow()

    /**
     * Atualiza o valor do e-mail conforme o usuário digita.
     */
    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    /**
     * Atualiza o valor da senha conforme o usuário digita.
     */
    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    /**
     * Inicia o processo de login.
     * Lançado em uma coroutine para não bloquear a thread principal.
     */
    fun login() {
        viewModelScope.launch {
            // 1. Atualiza o estado para Loading, a UI reagirá mostrando um indicador.
            _loginUiState.value = LoginUiState.Loading
            
            try {
                // 2. Monta o objeto de requisição com os dados atuais dos StateFlows.
                val loginRequest = LoginRequest(
                    email = _email.value,
                    password = _password.value
                )
                
                // 3. Chama a função de suspensão do repositório.
                val authResponse = authRepository.fazerLogin(loginRequest)
                
                // 4. Em caso de sucesso, atualiza o estado com o token recebido.
                _loginUiState.value = LoginUiState.Success(authResponse.token)
                
            } catch (e: Exception) {
                // 5. Em caso de falha (rede, 4xx, 5xx), atualiza o estado com a mensagem de erro.
                _loginUiState.value = LoginUiState.Error(e.message ?: "Ocorreu um erro desconhecido.")
            }
        }
    }
    
    /**
     * Reseta o estado da UI para Idle. Útil após uma navegação ou para limpar um erro.
     */
    fun resetState() {
        _loginUiState.value = LoginUiState.Idle
    }
}