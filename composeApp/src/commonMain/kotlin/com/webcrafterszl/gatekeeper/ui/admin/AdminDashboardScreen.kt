package com.webcrafterszl.gatekeeper.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.webcrafterszl.gatekeeper.data.model.CreateManagerRequest
import com.webcrafterszl.gatekeeper.ui.components.AppButton
import com.webcrafterszl.gatekeeper.ui.components.AppTextField
import com.webcrafterszl.gatekeeper.viewmodel.AdminUiState
import com.webcrafterszl.gatekeeper.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: AdminViewModel,
    onLogoutClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    // Efeito para mostrar snackbar em caso de sucesso ou erro
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AdminUiState.Success -> {
                snackbarHostState.showSnackbar("Gestor criado com sucesso!")
                fullName = ""
                email = ""
                viewModel.resetState()
            }
            is AdminUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Painel do Administrador") },
                actions = {
                    TextButton(onClick = onLogoutClick) { Text("Sair") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Criar Novo Gestor", style = MaterialTheme.typography.headlineSmall)
            
            Spacer(Modifier.height(24.dp))

            AppTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Nome Completo do Gestor"
            )
            
            Spacer(Modifier.height(16.dp))

            AppTextField(
                value = email,
                onValueChange = { email = it },
                label = "E-mail do Gestor"
            )

            Spacer(Modifier.height(24.dp))

            if (uiState is AdminUiState.Loading) {
                CircularProgressIndicator()
            } else {
                AppButton(
                    text = "Criar Gestor",
                    onClick = {
                        val request = CreateManagerRequest(fullName, email)
                        viewModel.createManager(request)
                    },
                    enabled = fullName.isNotBlank() && email.isNotBlank()
                )
            }
        }
    }
}