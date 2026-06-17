package com.webcrafterszl.gatekeeper.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.webcrafterszl.gatekeeper.ui.components.GatekeeperHeaderCard
import com.webcrafterszl.gatekeeper.ui.components.GatekeeperScreenBackground

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

	GatekeeperScreenBackground {
		Column(
			modifier = Modifier.fillMaxSize().padding(24.dp),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			GatekeeperHeaderCard(
				title = "Criar senha",
				subtitle = "Defina uma nova senha para o e-mail $email.",
				badgeText = "Etapa 3 de 3",
				modifier = Modifier.fillMaxWidth().widthIn(max = 520.dp),
			) {
				AppTextField(
					value = password,
					onValueChange = { password = it },
					label = "Nova senha",
					modifier = Modifier.padding(top = 8.dp),
					secureText = true,
					isError = saveAttempted && !isPasswordValid,
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
					label = "Confirmar senha",
					modifier = Modifier.padding(top = 12.dp),
					secureText = true,
					isError = saveAttempted && !passwordsMatch,
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
					text = "Salvar senha",
					modifier = Modifier.fillMaxWidth().padding(top = 24.dp).height(52.dp),
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
					modifier = Modifier.fillMaxWidth().padding(top = 12.dp).height(52.dp),
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