package com.webcrafterszl.gatekeeper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webcrafterszl.gatekeeper.data.model.LoginRequest
import com.webcrafterszl.gatekeeper.data.remote.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Define os possíveis estados da UI para a tela de login.
 * Usar uma sealed interface garante um tratamento de estados seguro no Compose.
 */
sealed interface LoginUiState {
    object Idle : LoginUiState // Estado inicial, aguardando interação.
    object Loading : LoginUiState // Estado de carregamento, aguardando a resposta da rede.
    data class Success(val token: String) : LoginUiState // Sucesso, contém o token para navegação.
    data class Error(val message: String) : LoginUiState // Erro, contém uma mensagem para o usuário.
}

/**
 * ViewModel para a tela de Login.
 * Responsável por gerenciar o estado da UI e se comunicar com o AuthRepository.
 */
class AuthViewModel(
    // A injeção do repositório facilita os testes e a manutenção.
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    // Fluxo de estado privado e mutável para o campo de e-mail.
    private val _email = MutableStateFlow("")
    // Exposição pública e imutável do estado do e-mail para a UI.
    val email = _email.asStateFlow()

    // Fluxo de estado privado e mutável para o campo de senha.
    private val _password = MutableStateFlow("")
    // Exposição pública e imutável do estado da senha para a UI.
    val password = _password.asStateFlow()

    // Fluxo de estado para o estado geral da UI (Idle, Loading, Success, Error).
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState = _loginUiState.asStateFlow()

    /**
     * Atualiza o valor do e-mail conforme o usuário digita no campo de texto.
     */
    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    /**
     * Atualiza o valor da senha conforme o usuário digita no campo de texto.
     */
    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    /**
     * Inicia o processo de login.
     * A função é executada em uma coroutine para não bloquear a thread principal.
     */
    fun login() {
        viewModelScope.launch {
            // 1. Atualiza o estado para Loading, a UI reagirá mostrando um indicador de progresso.
            _loginUiState.value = LoginUiState.Loading
            
            try {
                // 2. Monta o objeto de requisição com os dados atuais dos StateFlows.
                val loginRequest = LoginRequest(
                    email = _email.value,
                    password = _password.value
                )
                
                // 3. Chama a função de suspensão do repositório para autenticar.
                val authResponse = authRepository.login(loginRequest)
                
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