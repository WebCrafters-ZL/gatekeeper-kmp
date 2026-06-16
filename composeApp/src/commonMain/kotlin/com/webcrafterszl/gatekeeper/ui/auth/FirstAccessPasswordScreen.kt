package com.webcrafterszl.gatekeeper.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.webcrafterszl.gatekeeper.ui.components.AppButton
import com.webcrafterszl.gatekeeper.ui.components.AppTextField

@Composable
fun FirstAccessPasswordScreen(
    email: String,
	onSetPasswordClick: (String) -> Unit,
	onBackClick: () -> Unit,
) {
	var password by remember { mutableStateOf("") }
	var confirmPassword by remember { mutableStateOf("") }
	var saveAttempted by remember { mutableStateOf(false) }
	val colorScheme = MaterialTheme.colorScheme

	val isPasswordValid = password.length >= 6
	val passwordsMatch = password == confirmPassword
	val isFormValid = isPasswordValid && passwordsMatch

	Box(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
		Column(
			modifier = Modifier.fillMaxSize().padding(24.dp),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Surface(
				modifier = Modifier.fillMaxWidth().widthIn(max = 460.dp),
				color = colorScheme.surface,
				shape = RoundedCornerShape(20.dp),
				tonalElevation = 2.dp,
			) {
				Column(modifier = Modifier.padding(20.dp)) {
					Text(
						text = "Cadastrar Senha",
						style = MaterialTheme.typography.headlineSmall,
						color = colorScheme.tertiary,
						fontWeight = FontWeight.SemiBold,
					)
					Text(
						text = "Crie uma senha para o e-mail $email.",
						style = MaterialTheme.typography.bodyMedium,
						color = colorScheme.onSurfaceVariant,
						modifier = Modifier.padding(top = 6.dp),
					)

					AppTextField(
						value = password,
						onValueChange = { password = it },
						label = "Nova Senha",
						modifier = Modifier.padding(top = 20.dp),
						secureText = true,
						isError = saveAttempted && !isPasswordValid
					)
					if (saveAttempted && !isPasswordValid) {
						Text(
							text = "A senha deve ter pelo menos 6 caracteres.",
							style = MaterialTheme.typography.bodySmall,
							color = colorScheme.error,
							modifier = Modifier.padding(top = 6.dp),
						)
					}

					AppTextField(
						value = confirmPassword,
						onValueChange = { confirmPassword = it },
						label = "Confirmar Senha",
						modifier = Modifier.padding(top = 12.dp),
						secureText = true,
						isError = saveAttempted && !passwordsMatch
					)
					if (saveAttempted && !passwordsMatch) {
						Text(
							text = "As senhas não coincidem.",
							style = MaterialTheme.typography.bodySmall,
							color = colorScheme.error,
							modifier = Modifier.padding(top = 6.dp),
						)
					}

					AppButton(
						text = "Salvar Senha",
						modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
						enabled = isFormValid,
						onClick = {
							saveAttempted = true
							if (isFormValid) {
								onSetPasswordClick(password)
							}
						},
					)

					OutlinedButton(
						onClick = onBackClick,
						modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
						colors = ButtonDefaults.outlinedButtonColors(
							contentColor = colorScheme.primary,
						),
					) {
						Text("Voltar")
					}
				}
			}
		}
	}
}