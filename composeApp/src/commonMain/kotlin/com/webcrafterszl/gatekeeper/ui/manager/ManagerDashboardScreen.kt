package com.webcrafterszl.gatekeeper.ui.manager

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.webcrafterszl.gatekeeper.viewmodel.ManagerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerDashboardScreen(
    viewModel: ManagerViewModel,
    onLogoutClick: () -> Unit
) {
    // Variável para controlar a aba selecionada no BottomNavigation
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Logs", "Portadores", "Pontos", "Credenciais")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Painel do Gestor") },
                actions = {
                    TextButton(onClick = onLogoutClick) { Text("Sair") }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        icon = { /* Ícones omitidos para simplicidade */ },
                        label = { Text(title) }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
            // Renderiza o conteúdo baseado na aba selecionada
            when (selectedTabIndex) {
                0 -> Text("Lista de Todos os Logs do Sistema")
                1 -> Text("CRUD de Portadores")
                2 -> Text("Gestão de Pontos de Acesso")
                3 -> Text("Gestão de Credenciais RFID (Bloqueio via MQTT)")
            }
        }
    }
}