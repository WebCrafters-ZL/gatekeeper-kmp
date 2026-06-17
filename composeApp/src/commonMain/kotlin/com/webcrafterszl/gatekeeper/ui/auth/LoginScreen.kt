package com.webcrafterszl.gatekeeper.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.webcrafterszl.gatekeeper.data.model.Role
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
    onLoginSuccess: (Role) -> Unit, // O callback agora espera um Role, não mais uma String de token.
    onFirstAccessClick: () -> Unit,
    logoContent: (@Composable () -> Unit)? = null,
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val loginState by viewModel.loginUiState.collectAsState()

    val colorScheme = MaterialTheme.colorScheme

    // O LaunchedEffect agora reage ao estado de sucesso e passa o perfil (Role).
    LaunchedEffect(loginState) {
        if (loginState is LoginUiState.Success) {
            val userRole = (loginState as LoginUiState.Success).role
            onLoginSuccess(userRole)
            viewModel.resetState()
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

                    AppTextField(
                        value = password,
                        onValueChange = { viewModel.onPasswordChange(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        label = "Senha",
                        secureText = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        )
                    )

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
                        else -> {}
                    }

                    AppButton(
                        text = "Entrar",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                            .height(50.dp),
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