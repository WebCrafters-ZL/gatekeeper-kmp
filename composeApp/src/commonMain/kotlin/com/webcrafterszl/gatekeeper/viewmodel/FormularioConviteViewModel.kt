package com.webcrafterszl.gatekeeper.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FormularioConviteViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var isSuccess by mutableStateOf(false)
        private set

    fun enviarConvite(nome: String, email: String, cpf: String, data: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            // Lógica de chamada ao repositório viria aqui
            // Por enquanto, vamos simular um sucesso para manter a UI funcional
            kotlinx.coroutines.delay(1000) // Simula chamada de rede
            isSuccess = true
            isLoading = false
        }
    }

    fun onErrorMessageShown() {
        errorMessage = null
    }

    fun onSuccessMessageShown() {
        isSuccess = false
    }
}