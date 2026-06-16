package com.webcrafterszl.gatekeeper.ui.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.webcrafterszl.gatekeeper.ui.components.AppButton

data class AccessLog(
    val tagRead: String,
    val accessPointId: String,
    val timestamp: String,
    val isGranted: Boolean,
    val denialReason: String?
)

@Composable
fun AccessLogScreen(onBack: () -> Unit) {
    val accessLogs = listOf(
        AccessLog("A1B2C3D4", "CATRACA_01", "2023-10-27 10:00:00", true, null),
        AccessLog("E5F6G7H8", "CATRACA_02", "2023-10-27 10:01:00", false, "Credencial expirada"),
        AccessLog("I9J0K1L2", "PORTA_01", "2023-10-27 10:02:00", true, null)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dashboard de Logs de Acesso", style = MaterialTheme.typography.headlineMedium)
            AppButton(text = "Voltar", onClick = onBack)
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tag", modifier = Modifier.weight(1f))
                    Text("Ponto de Acesso", modifier = Modifier.weight(1f))
                    Text("Horário", modifier = Modifier.weight(1f))
                    Text("Status", modifier = Modifier.weight(0.5f))
                }
            }
            items(accessLogs) { log ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(log.tagRead, modifier = Modifier.weight(1f))
                    Text(log.accessPointId, modifier = Modifier.weight(1f))
                    Text(log.timestamp, modifier = Modifier.weight(1f))
                    Text(
                        if (log.isGranted) "Concedido" else "Negado",
                        color = if (log.isGranted) Color.Green else Color.Red,
                        modifier = Modifier.weight(0.5f)
                    )
                }
                log.denialReason?.let {
                    Text("Motivo: $it", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }
    }
}