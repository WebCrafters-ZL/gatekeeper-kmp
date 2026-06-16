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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.webcrafterszl.gatekeeper.ui.components.AppButton
import com.webcrafterszl.gatekeeper.ui.components.AppTextField
import com.webcrafterszl.gatekeeper.viewmodel.AuthViewModel
import com.webcrafterszl.gatekeeper.viewmodel.LoginUiState
import gatekeeper.composeapp.generated.resources.Res
import gatekeeper.composeapp.generated.resources.gatekeeper_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel { AuthViewModel() },
    onLoginSuccess: (String) -> Unit,
    onFirstAccessClick: () -> Unit,
    logoContent: (@Composable () -> Unit)? = null,
) {
    // Observa os estados do ViewModel usando o delegate 'by'. 
    // A UI será recomposta automaticamente quando eles mudarem.
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val loginState by viewModel.loginUiState.collectAsState()

    val colorScheme = MaterialTheme.colorScheme

    // LaunchedEffect é usado para lidar com "side effects", como a navegação.
    // Ele será executado sempre que o 'loginState' mudar.
    LaunchedEffect(loginState) {
        if (loginState is LoginUiState.Success) {
            // Extrai o token do estado de sucesso e chama a função de navegação.
            val token = (loginState as LoginUiState.Success).token
            onLoginSuccess(token)
            viewModel.resetState() // Limpa o estado para evitar re-navegação em recomposições.
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
                    // Campo de E-mail conectado ao ViewModel
                    AppTextField(
                        value = email,
                        onValueChange = { viewModel.onEmailChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = "E-mail",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                        )
                    )

                    // Campo de Senha conectado ao ViewModel
                    AppTextField(
                        value = password,
                        onValueChange = { viewModel.onPasswordChange(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        label = "Senha",
                        secureText = true, // AppTextField já trata a lógica de mostrar/ocultar
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        )
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
                        else -> {} // Idle e Success não precisam renderizar nada aqui.
                    }

                    // Botão de ação principal conectado ao ViewModel
                    AppButton(
                        text = "Entrar",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                            .height(50.dp),
                        // O botão fica desabilitado enquanto carrega ou se os campos estiverem vazios.
                        enabled = loginState !is LoginUiState.Loading && email.isNotBlank() && password.isNotBlank(),
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