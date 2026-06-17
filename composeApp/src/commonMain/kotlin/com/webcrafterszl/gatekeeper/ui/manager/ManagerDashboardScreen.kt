package com.webcrafterszl.gatekeeper.ui.manager

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.webcrafterszl.gatekeeper.viewmodel.ManagerViewModel
import com.webcrafterszl.gatekeeper.ui.components.GatekeeperHeaderCard
import com.webcrafterszl.gatekeeper.ui.components.GatekeeperMetricPill
import com.webcrafterszl.gatekeeper.ui.components.GatekeeperScreenBackground

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
        GatekeeperScreenBackground {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                GatekeeperHeaderCard(
                    title = "Painel de gestão",
                    subtitle = "Monitore eventos, cadastros e pontos de acesso em uma única interface.",
                    badgeText = "MANAGER",
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        GatekeeperMetricPill(label = "Logs", value = "Centralizados", modifier = Modifier.weight(1f))
                        GatekeeperMetricPill(label = "IoT", value = "Sincronização MQTT", modifier = Modifier.weight(1f))
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    tonalElevation = 4.dp,
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        when (selectedTabIndex) {
                            0 -> Text("Lista de Todos os Logs do Sistema")
                            1 -> Text("CRUD de Portadores")
                            2 -> Text("Gestão de Pontos de Acesso")
                            3 -> Text("Gestão de Credenciais RFID (Bloqueio via MQTT)")
                        }
                    }
                }
            }
        }
    }
}