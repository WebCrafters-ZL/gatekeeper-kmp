package com.webcrafterszl.gatekeeper.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.webcrafterszl.gatekeeper.ui.components.AppButton

@OptIn(ExperimentalMaterial3Api::class) // Correção para o aviso de API experimental
@Composable
fun UserDashboardScreen(
    onConvidarVisitanteClick: () -> Unit,
    onHistoricoAcessosClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Painel do Usuário") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions = {
                    AppButton(
                        text = "Sair",
                        onClick = onLogoutClick,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Bem-vindo ao Gatekeeper!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(32.dp))

            AppButton(
                text = "Convidar Visitante",
                onClick = onConvidarVisitanteClick,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            AppButton(
                text = "Histórico de Acessos",
                onClick = onHistoricoAcessosClick,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            )
        }
    }
}