package com.webcrafterszl.gatekeeper.ui.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.webcrafterszl.gatekeeper.ui.components.AppButton
import com.webcrafterszl.gatekeeper.ui.components.AppTextField

data class Cardholder(
    val id: String,
    val name: String,
    val email: String,
    var isActive: Boolean
)

@Composable
fun CardholderScreen(onBack: () -> Unit) {
    val cardholders = remember {
        mutableStateListOf(
            Cardholder("1", "Leonardo", "leo@email.com", true),
            Cardholder("2", "Silva", "silva@email.com", false),
        )
    }
    
    var showForm by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

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
            Text("Gestão de Portadores", style = MaterialTheme.typography.headlineMedium)
            AppButton(text = "Voltar", onClick = onBack)
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        if (showForm) {
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                AppTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nome"
                )
                Spacer(modifier = Modifier.height(8.dp))
                AppTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppButton(text = "Salvar", onClick = {
                        if (name.isNotBlank() && email.isNotBlank()) {
                            cardholders.add(Cardholder(id = cardholders.size.plus(1).toString(), name = name, email = email, isActive = true))
                            showForm = false
                            name = ""
                            email = ""
                        }
                    })
                    AppButton(text = "Cancelar", onClick = { showForm = false })
                }
            }
        } else {
            AppButton(text = "Novo Portador", onClick = { showForm = true })
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
                    Text("Nome", modifier = Modifier.weight(1f))
                    Text("Email", modifier = Modifier.weight(1f))
                    Text("Status", modifier = Modifier.weight(0.5f))
                }
            }
            items(cardholders) { cardholder ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(cardholder.name, modifier = Modifier.weight(1f))
                    Text(cardholder.email, modifier = Modifier.weight(1f))
                    Row(modifier = Modifier.weight(0.5f), verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = cardholder.isActive,
                            onCheckedChange = { cardholder.isActive = it }
                        )
                        Text(if (cardholder.isActive) "Ativo" else "Inativo", color = if (cardholder.isActive) Color.Green else Color.Red)
                    }
                }
            }
        }
    }
}