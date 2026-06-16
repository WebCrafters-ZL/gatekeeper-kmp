package com.webcrafterszl.gatekeeper.ui.cardholder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.webcrafterszl.gatekeeper.data.model.CardholderAccessLogResponse
import com.webcrafterszl.gatekeeper.viewmodel.CardholderUiState
import com.webcrafterszl.gatekeeper.viewmodel.CardholderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardholderDashboardScreen(
    viewModel: CardholderViewModel,
    onLogoutClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.fetchOwnAccessLogs()
    }

    LaunchedEffect(uiState) {
        if (uiState is CardholderUiState.Error) {
            val msg = (uiState as CardholderUiState.Error).message
            snackbarHostState.showSnackbar(msg)
            viewModel.resetState()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Meu Painel") },
                actions = {
                    TextButton(onClick = onLogoutClick) { Text("Sair") }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                "Meus Acessos",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when (val state = uiState) {
                is CardholderUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is CardholderUiState.Success -> {
                    if (state.logs.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Nenhum registro encontrado.")
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(state.logs) { log ->
                                CardholderAccessLogCard(log)
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun CardholderAccessLogCard(log: CardholderAccessLogResponse) {
    val statusColor = if (log.isGranted) Color(0xFF4CAF50) else Color(0xFFF44336)
    val statusText = if (log.isGranted) "Permitido" else "Negado"

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Ponto: #${log.accessPointId}", fontWeight = FontWeight.Bold)
                Text("Data: ${log.timestamp}", style = MaterialTheme.typography.bodyMedium)
            }
            Surface(color = statusColor.copy(alpha = 0.1f), shape = MaterialTheme.shapes.small) {
                Text(
                    text = statusText,
                    color = statusColor,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
        if (!log.isGranted && log.denialReason != null) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            Text(
                text = "Motivo: ${log.denialReason}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}