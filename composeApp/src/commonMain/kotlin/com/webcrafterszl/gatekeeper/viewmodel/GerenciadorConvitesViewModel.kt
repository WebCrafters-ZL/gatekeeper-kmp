package com.webcrafterszl.gatekeeper.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.webcrafterszl.gatekeeper.data.model.Convite
import com.webcrafterszl.gatekeeper.data.remote.RepositorioRemoto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Simulação do ID do usuário logado. Em um app real, isso viria de uma sessão.
private const val LOGGED_IN_USER_ID = 1 

class GerenciadorConvitesViewModel(
    private val repositorio: RepositorioRemoto = RepositorioRemoto(),
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    private val _convites = MutableStateFlow<List<Convite>>(emptyList())
    val convites = _convites.asStateFlow()

    var linkGerado by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        carregarConvites()
    }

    fun carregarConvites() {
        coroutineScope.launch {
            isLoading = true
            errorMessage = null
            try {
                _convites.value = repositorio.listarConvites(LOGGED_IN_USER_ID)
            } catch (e: Exception) {
                errorMessage = "Erro ao carregar convites: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun gerarNovoConvite(dataVisita: String) {
        coroutineScope.launch {
            isLoading = true
            errorMessage = null
            linkGerado = null
            try {
                linkGerado = repositorio.criarConvite(LOGGED_IN_USER_ID, dataVisita)
                // Após gerar um novo, atualiza a lista
                carregarConvites()
            } catch (e: Exception) {
                errorMessage = "Erro ao gerar link: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun onErrorMessageShown() {
        errorMessage = null
    }
}