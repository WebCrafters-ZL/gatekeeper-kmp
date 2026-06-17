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
fun FirstAccessEmailScreen(
	onNextClick: (String) -> Unit,
	onBackClick: () -> Unit,
) {
	var email by remember { mutableStateOf("") }
	var submissionAttempted by remember { mutableStateOf(false) }
	val colorScheme = MaterialTheme.colorScheme

	val isEmailValid = email.isNotBlank() && "@" in email && "." in email

	GatekeeperScreenBackground {
		Column(
			modifier = Modifier.fillMaxSize().padding(24.dp),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			GatekeeperHeaderCard(
				title = "Primeiro acesso",
				subtitle = "Informe seu e-mail corporativo para receber o código de verificação.",
				badgeText = "Etapa 1 de 3",
				modifier = Modifier.fillMaxWidth().widthIn(max = 520.dp),
			) {
				AppTextField(
					value = email,
					onValueChange = { email = it },
					label = "E-mail",
					modifier = Modifier.padding(top = 8.dp),
					isError = submissionAttempted && !isEmailValid,
				)
				if (submissionAttempted && !isEmailValid) {
					Text(
						text = "Informe um e-mail válido para continuar.",
						style = MaterialTheme.typography.bodySmall,
						color = colorScheme.error,
						modifier = Modifier.padding(top = 6.dp),
					)
				}

				AppButton(
					text = "Enviar Código",
					modifier = Modifier.fillMaxWidth().padding(top = 24.dp).height(52.dp),
					enabled = isEmailValid,
					onClick = {
						submissionAttempted = true
						if (isEmailValid) {
							onNextClick(email)
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
					Text("Voltar para login")
				}
			}
		}
	}
}