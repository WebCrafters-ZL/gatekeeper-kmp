package com.webcrafterszl.gatekeeper.viewmodel

import com.webcrafterszl.gatekeeper.data.model.CardholderAccessLogResponse
import com.webcrafterszl.gatekeeper.data.remote.CardholderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

sealed interface CardholderUiState {
    object Idle : CardholderUiState
    object Loading : CardholderUiState
    data class Success(val logs: List<CardholderAccessLogResponse>) : CardholderUiState
    data class Error(val message: String) : CardholderUiState
}

/**
 * ViewModel para as operações do Portador (Cardholder).
 */
class CardholderViewModel(
    private val cardholderRepository: CardholderRepository = CardholderRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<CardholderUiState>(CardholderUiState.Idle)
    val uiState = _uiState.asStateFlow()

    // Token simulado
    private val token = "mock-cardholder-token"

    /**
     * Busca o histórico de acessos do próprio usuário.
     */
    fun fetchOwnAccessLogs() {
        viewModelScope.launch {
            _uiState.value = CardholderUiState.Loading
            try {
                val logs = cardholderRepository.getOwnAccessLogs(token)
                _uiState.value = CardholderUiState.Success(logs)
            } catch (e: Exception) {
                _uiState.value = CardholderUiState.Error(e.message ?: "Erro ao carregar histórico.")
            }
        }
    }

    fun resetState() {
        _uiState.value = CardholderUiState.Idle
    }
}