package com.webcrafterszl.gatekeeper.ui.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.webcrafterszl.gatekeeper.ui.components.AppButton
import com.webcrafterszl.gatekeeper.ui.components.AppTextField

data class AccessPoint(
    val id: String,
    val mqttIdentifier: String,
    val locationDescription: String
)

@Composable
fun AccessPointScreen(onBack: () -> Unit) {
    val accessPoints = remember {
        mutableStateListOf(
            AccessPoint("1", "CATRACA_01", "Portaria Principal"),
            AccessPoint("2", "CATRACA_02", "Entrada Lateral"),
        )
    }
    
    var showForm by remember { mutableStateOf(false) }
    var mqttIdentifier by remember { mutableStateOf("") }
    var locationDescription by remember { mutableStateOf("") }

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
            Text("Gestão de Pontos de Acesso", style = MaterialTheme.typography.headlineMedium)
            AppButton(text = "Voltar", onClick = onBack)
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        if (showForm) {
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                AppTextField(
                    value = mqttIdentifier,
                    onValueChange = { mqttIdentifier = it },
                    label = "Identificador MQTT"
                )
                Spacer(modifier = Modifier.height(8.dp))
                AppTextField(
                    value = locationDescription,
                    onValueChange = { locationDescription = it },
                    label = "Descrição do Local"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppButton(text = "Salvar", onClick = {
                        if (mqttIdentifier.isNotBlank() && locationDescription.isNotBlank()) {
                            accessPoints.add(AccessPoint(id = accessPoints.size.plus(1).toString(), mqttIdentifier = mqttIdentifier, locationDescription = locationDescription))
                            showForm = false
                            mqttIdentifier = ""
                            locationDescription = ""
                        }
                    })
                    AppButton(text = "Cancelar", onClick = { showForm = false })
                }
            }
        } else {
            AppButton(text = "Novo Ponto de Acesso", onClick = { showForm = true })
            Spacer(modifier = Modifier.height(16.dp))
        }

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
                    Text("MQTT ID", modifier = Modifier.weight(1f))
                    Text("Local", modifier = Modifier.weight(1f))
                }
            }
            items(accessPoints) { point ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(point.mqttIdentifier, modifier = Modifier.weight(1f))
                    Text(point.locationDescription, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}