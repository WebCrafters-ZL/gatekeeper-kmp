package com.webcrafterszl.gatekeeper.viewmodel

import com.webcrafterszl.gatekeeper.data.model.*
import com.webcrafterszl.gatekeeper.data.remote.ManagerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

sealed interface ManagerUiState {
    object Idle : ManagerUiState
    object Loading : ManagerUiState
    object Success : ManagerUiState
    data class Error(val message: String) : ManagerUiState
}

/**
 * ViewModel que gerencia os fluxos do perfil Gestor (Manager).
 * Ele controla o estado das listas de pontos de acesso, portadores, credenciais e logs.
 */
class ManagerViewModel(
    private val managerRepository: ManagerRepository = ManagerRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ManagerUiState>(ManagerUiState.Idle)
    val uiState = _uiState.asStateFlow()

    // Estados para as listagens
    private val _accessLogs = MutableStateFlow<List<AccessLogResponse>>(emptyList())
    val accessLogs = _accessLogs.asStateFlow()

    private val _cardholders = MutableStateFlow<List<CardholderResponse>>(emptyList())
    val cardholders = _cardholders.asStateFlow()

    private val _accessPoints = MutableStateFlow<List<AccessPointResponse>>(emptyList())
    val accessPoints = _accessPoints.asStateFlow()

    // Token simulado (em um cenário real, injetado ou lido de storage)
    private val token = "mock-manager-token"

    /**
     * Busca a lista paginada de todos os logs de acesso.
     */
    fun fetchAccessLogs() {
        viewModelScope.launch {
            _uiState.value = ManagerUiState.Loading
            try {
                _accessLogs.value = managerRepository.getAccessLogs(token)
                _uiState.value = ManagerUiState.Idle
            } catch (e: Exception) {
                _uiState.value = ManagerUiState.Error(e.message ?: "Erro ao carregar logs.")
            }
        }
    }

    /**
     * Busca a lista de portadores (cardholders).
     */
    fun fetchCardholders() {
        viewModelScope.launch {
            _uiState.value = ManagerUiState.Loading
            try {
                _cardholders.value = managerRepository.getCardholders(token)
                _uiState.value = ManagerUiState.Idle
            } catch (e: Exception) {
                _uiState.value = ManagerUiState.Error(e.message ?: "Erro ao carregar portadores.")
            }
        }
    }

    /**
     * Atualiza o status (bloqueado/desbloqueado) de uma credencial.
     * Isso dispara uma ação no back-end que reflete via MQTT nas catracas.
     */
    fun toggleCredentialStatus(credentialId: Long, isBlocked: Boolean) {
        viewModelScope.launch {
            _uiState.value = ManagerUiState.Loading
            try {
                managerRepository.updateCredentialStatus(token, credentialId, isBlocked)
                _uiState.value = ManagerUiState.Success
                // Em um app real, faríamos um refresh na lista de credenciais aqui
            } catch (e: Exception) {
                _uiState.value = ManagerUiState.Error(e.message ?: "Erro ao atualizar credencial.")
            }
        }
    }

    fun resetState() {
        _uiState.value = ManagerUiState.Idle
    }
}