package com.webcrafterszl.gatekeeper.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.webcrafterszl.gatekeeper.ui.components.AppButton

data class AcessoMock(
    val id: Int,
    val nomeVisitante: String,
    val dataAcesso: String,
    val horaAcesso: String,
    val tipo: String // "Entrada" ou "Saída"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricoAcessosScreen(
    onBack: () -> Unit
) {
    // Dados mockados para visualização
    val historico = remember {
        listOf(
            AcessoMock(1, "João Silva", "10/11/2024", "08:30", "Entrada"),
            AcessoMock(2, "Maria Oliveira", "10/11/2024", "09:15", "Entrada"),
            AcessoMock(3, "João Silva", "10/11/2024", "12:00", "Saída"),
            AcessoMock(4, "Carlos Souza", "11/11/2024", "14:20", "Entrada"),
            AcessoMock(5, "Maria Oliveira", "11/11/2024", "17:45", "Saída")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico de Acessos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    AppButton(
                        text = "Voltar",
                        onClick = onBack,
                        modifier = Modifier.padding(start = 8.dp)
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
                .padding(16.dp)
        ) {
            Text(
                text = "Últimos acessos registrados (Mock)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(historico) { acesso ->
                    CardAcesso(acesso)
                }
            }
        }
    }
}

@Composable
fun CardAcesso(acesso: AcessoMock) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = acesso.nomeVisitante,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${acesso.dataAcesso} às ${acesso.horaAcesso}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }
            
            // Badge para Entrada/Saída
            val badgeColor = if (acesso.tipo == "Entrada") {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.tertiary
            }
            
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = badgeColor.copy(alpha = 0.1f),
                border = androidx.compose.foundation.BorderStroke(1.dp, badgeColor)
            ) {
                Text(
                    text = acesso.tipo,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = badgeColor,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}