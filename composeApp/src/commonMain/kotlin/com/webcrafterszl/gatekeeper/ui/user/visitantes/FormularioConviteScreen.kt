package com.webcrafterszl.gatekeeper.ui.user.visitantes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.webcrafterszl.gatekeeper.ui.components.AppButton
import com.webcrafterszl.gatekeeper.ui.components.AppTextField
import com.webcrafterszl.gatekeeper.ui.utils.CpfVisualTransformation
import com.webcrafterszl.gatekeeper.viewmodel.FormularioConviteViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioConviteScreen(
    viewModel: FormularioConviteViewModel,
    onBack: () -> Unit
) {
    var nomeCompleto by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var dataVisita by remember { mutableStateOf("") }

    var tentouEnviar by remember { mutableStateOf(false) }

    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val isSuccess = viewModel.isSuccess

    val snackbarHostState = remember { SnackbarHostState() }

    // DatePicker State
    var showDatePicker by remember { mutableStateOf(false) }
    
    // É importante usar o remember para o SelectableDates, caso contrário o DatePicker 
    // pode entrar em um loop de recomposição pesado ao desenhar o calendário.
    val selectableDates = remember {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val todayMillis = Clock.System.now().toEpochMilliseconds()
                // Dá uma folga de 24h para fuso horários, permitindo escolher hoje.
                return utcTimeMillis >= todayMillis - 86400000 
            }
        }
    }
    
    val datePickerState = rememberDatePickerState(selectableDates = selectableDates)

    // Validações
    val nomeValido = nomeCompleto.isNotBlank() && nomeCompleto.length > 2
    val emailValido = email.isNotBlank() && email.contains("@") && email.contains(".")
    val cpfValido = cpf.length == 11
    val dataValida = dataVisita.isNotBlank()

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onErrorMessageShown()
        }
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            snackbarHostState.showSnackbar("Convite enviado com sucesso!")
            nomeCompleto = ""
            email = ""
            cpf = ""
            dataVisita = ""
            tentouEnviar = false
            viewModel.onSuccessMessageShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Convite para Visitante") },
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Preencha os dados do visitante para o acesso de um único dia.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            AppTextField(
                value = nomeCompleto,
                onValueChange = { nomeCompleto = it },
                label = "Nome Completo",
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                isError = tentouEnviar && !nomeValido,
                supportingText = "Insira o nome completo."
            )

            AppTextField(
                value = email,
                onValueChange = { email = it },
                label = "E-mail",
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = tentouEnviar && !emailValido,
                supportingText = "Insira um e-mail válido."
            )

            AppTextField(
                value = cpf,
                onValueChange = { 
                    if (it.length <= 11 && it.all { char -> char.isDigit() }) {
                        cpf = it 
                    }
                },
                label = "CPF",
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = CpfVisualTransformation(),
                isError = tentouEnviar && !cpfValido,
                supportingText = "O CPF deve conter 11 dígitos."
            )

            // Usar um Box para englobar o TextField é a forma mais segura no Compose
            // para interceptar cliques sem que o componente TextField os consuma ou bloqueie.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                OutlinedTextField(
                    value = dataVisita,
                    onValueChange = { },
                    label = { Text("Data da Visita") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true, // Não usamos enabled=false para manter a estética normal e não bugar a árvore de acessibilidade/cliques
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Selecionar data"
                        )
                    },
                    isError = tentouEnviar && !dataValida,
                    supportingText = {
                        if (tentouEnviar && !dataValida) {
                            Text("Selecione uma data para a visita.")
                        }
                    }
                )
                
                // Uma camada invisível por cima de tudo que captura perfeitamente o clique
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { showDatePicker = true }
                )
            }

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
            }

            AppButton(
                text = "Enviar Convite por E-mail",
                onClick = {
                    tentouEnviar = true
                    if (nomeValido && emailValido && cpfValido && dataValida) {
                        viewModel.enviarConvite(nomeCompleto, email, cpf, dataVisita)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !isLoading
            )
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val selectedInstant = Instant.fromEpochMilliseconds(millis)
                                val dataSelecionada = selectedInstant.toLocalDateTime(TimeZone.currentSystemDefault())

                                // Monta a string formatada DD/MM/YYYY
                                val dia = dataSelecionada.dayOfMonth.toString().padStart(2, '0')
                                val mes = dataSelecionada.monthNumber.toString().padStart(2, '0')
                                val ano = dataSelecionada.year

                                dataVisita = "$dia/$mes/$ano"
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancelar")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}