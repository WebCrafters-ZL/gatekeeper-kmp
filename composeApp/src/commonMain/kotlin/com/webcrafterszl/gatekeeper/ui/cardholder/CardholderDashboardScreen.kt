package com.webcrafterszl.gatekeeper.ui.cardholder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.webcrafterszl.gatekeeper.data.model.CardholderAccessLogResponse
import com.webcrafterszl.gatekeeper.data.remote.CardholderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel simples para o CardholderDashboardScreen.
 * Em um projeto maior, este ViewModel seria movido para seu próprio arquivo na pasta 'viewmodel'.
 */
class CardholderDashboardViewModel(
    private val cardholderRepository: CardholderRepository = CardholderRepository()
) {
    private val _accessLogs = MutableStateFlow<List<CardholderAccessLogResponse>>(emptyList())
    val accessLogs: StateFlow<List<CardholderAccessLogResponse>> = _accessLogs

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // O token seria obtido de um local seguro após o login
    private val mockToken = "seu-jwt-token-aqui" 

    fun loadAccessLogs() {
        // Em um ViewModel real, usaríamos viewModelScope.launch
        // Aqui, para simplicidade, a coroutine será lançada no Composable.
        // Esta é uma simplificação para manter o exemplo contido em um único arquivo.
    }
    
    // Função de exemplo para carregar os dados (seria chamada no LaunchedEffect)
    suspend fun fetchAccessLogs() {
        _isLoading.value = true
        _errorMessage.value = null
        try {
            // No futuro, o token virá de um sistema de sessão
            _accessLogs.value = cardholderRepository.listOwnAccessLogs(mockToken)
        } catch (e: Exception) {
            _errorMessage.value = "Falha ao carregar o histórico: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardholderDashboardScreen(
    viewModel: CardholderDashboardViewModel = remember { CardholderDashboardViewModel() },
    onLogoutClick: () -> Unit
) {
    val accessLogs by viewModel.accessLogs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }

    // Carrega os logs quando a tela é exibida pela primeira vez
    LaunchedEffect(Unit) {
        viewModel.fetchAccessLogs()
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Meu Painel") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions = {
                    TextButton(onClick = onLogoutClick) {
                        Text("Sair")
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                "Histórico de Acessos",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (accessLogs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhum registro de acesso encontrado.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(accessLogs) { log ->
                        AccessLogCard(log)
                    }
                }
            }
        }
    }
}

@Composable
private fun AccessLogCard(log: CardholderAccessLogResponse) {
    val statusColor = if (log.isGranted) Color(0xFF4CAF50) else Color(0xFFF44336) // Verde e Vermelho
    val statusText = if (log.isGranted) "Permitido" else "Negado"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    // No futuro, teríamos um mapa ou um repositório para traduzir o ID do ponto de acesso para uma descrição
                    text = "Local: Ponto de Acesso #${log.accessPointId}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    // A data/hora viria formatada do ViewModel ou de uma função utilitária
                    text = "Data: ${log.timestamp}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            // Badge de Status
            Surface(
                color = statusColor.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = statusText,
                    color = statusColor,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
        if (!log.isGranted && log.denialReason != null) {
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
            Text(
                text = "Motivo: ${log.denialReason}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}