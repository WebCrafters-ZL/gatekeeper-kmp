package com.webcrafterszl.gatekeeper.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.webcrafterszl.gatekeeper.viewmodel.AuthViewModel
import com.webcrafterszl.gatekeeper.viewmodel.LoginUiState
import gatekeeper.composeapp.generated.resources.Res
import gatekeeper.composeapp.generated.resources.gatekeeper_logo
import org.jetbrains.compose.resources.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    // O ViewModel agora é o cérebro da tela.
    // Usamos a factory do viewModel() para criar uma instância que sobrevive a recomposições.
    viewModel: AuthViewModel = viewModel { AuthViewModel() },
    onLoginSuccess: (String) -> Unit,
    onFirstAccessClick: () -> Unit,
    logoContent: (@Composable () -> Unit)? = null,
) {
    // Observa os estados do ViewModel. A UI será recomposta automaticamente quando eles mudarem.
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val loginState by viewModel.loginUiState.collectAsState()

    var showPassword by remember { mutableStateOf(false) }
    val colorScheme = MaterialTheme.colorScheme

    // Lógica para navegar quando o estado for Success
    LaunchedEffect(loginState) {
        if (loginState is LoginUiState.Success) {
            val token = (loginState as LoginUiState.Success).token
            onLoginSuccess(token)
            viewModel.resetState() // Limpa o estado para evitar re-navegação
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.widthIn(max = 460.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (logoContent != null) {
                logoContent()
            } else {
                DefaultLoginLogo()
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                shape = RoundedCornerShape(20.dp),
                color = colorScheme.surface,
                tonalElevation = 2.dp,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Campo de E-mail vinculado ao ViewModel
                    OutlinedTextField(
                        value = email,
                        onValueChange = { viewModel.onEmailChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("E-mail") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                        ),
                        colors = loginTextFieldColors(),
                    )

                    // Campo de Senha vinculado ao ViewModel
                    OutlinedTextField(
                        value = password,
                        onValueChange = { viewModel.onPasswordChange(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        label = { Text("Senha") },
                        singleLine = true,
                        visualTransformation = if (showPassword) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        ),
                        trailingIcon = {
                            TextButton(onClick = { showPassword = !showPassword }) {
                                Text(if (showPassword) "Ocultar" else "Mostrar")
                            }
                        },
                        colors = loginTextFieldColors(),
                    )

                    // Tratamento de estados da UI (Loading e Error)
                    when (val state = loginState) {
                        is LoginUiState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                        is LoginUiState.Error -> {
                            Text(
                                text = state.message,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        else -> {} // Idle e Success não mostram nada aqui
                    }

                    PrimaryActionButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        // O botão fica desabilitado enquanto carrega
                        enabled = loginState !is LoginUiState.Loading,
                        onClick = { viewModel.login() },
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Primeiro acesso?",
                            color = colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        TextButton(onClick = onFirstAccessClick) {
                            Text("Cadastrar", color = colorScheme.primary, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DefaultLoginLogo() {
    Image(
        painter = painterResource(Res.drawable.gatekeeper_logo),
        contentDescription = "Logo Gatekeeper",
        modifier = Modifier.size(150.dp)
    )
}

@Composable
private fun PrimaryActionButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = PaddingValues(vertical = 14.dp),
        shape = RoundedCornerShape(14.dp),
    ) {
        Text(
            text = "Entrar",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun loginTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
)