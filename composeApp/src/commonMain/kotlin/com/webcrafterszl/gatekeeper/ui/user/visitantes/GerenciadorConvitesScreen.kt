package com.webcrafterszl.gatekeeper.ui.user.visitantes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.webcrafterszl.gatekeeper.data.model.Convite
import com.webcrafterszl.gatekeeper.ui.components.AppButton
import com.webcrafterszl.gatekeeper.viewmodel.GerenciadorConvitesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GerenciadorConvitesScreen(
    viewModel: GerenciadorConvitesViewModel,
    onBack: () -> Unit
) {
    val convites by viewModel.convites.collectAsState()
    val linkGerado = viewModel.linkGerado
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    // Para o Date Picker
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onErrorMessageShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Gerenciar Convites") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        // Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppButton(
                text = "Gerar Link de Pré-Cadastro",
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            )

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(vertical = 16.dp))
            }

            linkGerado?.let { link ->
                Spacer(modifier = Modifier.height(24.dp))
                LinkGeradoCard(
                    link = link,
                    onCopiarClick = {
                        clipboardManager.setText(AnnotatedString(link))
                        // Poderíamos mostrar um snackbar de "Link copiado!"
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Convites Enviados",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(convites, key = { it.id }) { convite ->
                    ConviteItem(convite)
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            // Converte millis para uma data legível.
                            // Em um app real, use uma lib de data/hora como kotlinx-datetime.
                            val data = "DataSelecionada" // Simples para o exemplo
                            viewModel.gerarNovoConvite(data)
                        }
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun LinkGeradoCard(link: String, onCopiarClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = link,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )
            IconButton(onClick = onCopiarClick) {
                Icon(
                    Icons.Default.ContentCopy,
                    contentDescription = "Copiar Link",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ConviteItem(convite: Convite) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Data da visita: ${convite.dataVisita}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Token: ${convite.token}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            StatusConvite(convite)
        }
    }
}

@Composable
private fun StatusConvite(convite: Convite) {
    val (text, color) = if (convite.visitanteId != null) {
        "Aceito" to MaterialTheme.colorScheme.primary
    } else {
        "Pendente" to MaterialTheme.colorScheme.tertiary
    }

    Text(
        text = text,
        color = color,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.bodyMedium
    )
    convite.nomeVisitante?.let {
        Text(
            text = " por $it",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}