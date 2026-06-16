package com.webcrafterszl.gatekeeper.viewmodel

import com.webcrafterszl.gatekeeper.data.model.CreateManagerRequest
import com.webcrafterszl.gatekeeper.data.remote.AdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

sealed interface AdminUiState {
    object Idle : AdminUiState
    object Loading : AdminUiState
    object Success : AdminUiState
    data class Error(val message: String) : AdminUiState
}

/**
 * ViewModel para as operações exclusivas do Administrador.
 */
class AdminViewModel(
    private val adminRepository: AdminRepository = AdminRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminUiState>(AdminUiState.Idle)
    val uiState = _uiState.asStateFlow()

    // Token simulado (em um app real, viria de injeção de dependência ou storage seguro)
    private val token = "mock-admin-token"

    fun createManager(request: CreateManagerRequest) {
        viewModelScope.launch {
            _uiState.value = AdminUiState.Loading
            try {
                adminRepository.createManager(token, request)
                _uiState.value = AdminUiState.Success
            } catch (e: Exception) {
                _uiState.value = AdminUiState.Error(e.message ?: "Erro ao criar gestor.")
            }
        }
    }

    fun resetState() {
        _uiState.value = AdminUiState.Idle
    }
}