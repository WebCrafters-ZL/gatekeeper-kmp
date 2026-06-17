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
import com.webcrafterszl.gatekeeper.ui.components.OtpTextField
import com.webcrafterszl.gatekeeper.ui.components.GatekeeperHeaderCard
import com.webcrafterszl.gatekeeper.ui.components.GatekeeperScreenBackground

@Composable
fun FirstAccessCodeScreen(
    email: String,
	onVerifyClick: (String) -> Unit,
	onBackClick: () -> Unit,
) {
	var otpValue by remember { mutableStateOf("") }
    var isOtpFilled by remember { mutableStateOf(false) }
	var verificationAttempted by remember { mutableStateOf(false) }
	val colorScheme = MaterialTheme.colorScheme

	GatekeeperScreenBackground {
		Column(
			modifier = Modifier.fillMaxSize().padding(24.dp),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			GatekeeperHeaderCard(
				title = "Verificação de acesso",
				subtitle = "Digite o código de 5 dígitos enviado para o e-mail $email.",
				badgeText = "Etapa 2 de 3",
				modifier = Modifier.fillMaxWidth().widthIn(max = 520.dp),
			) {
				OtpTextField(
					modifier = Modifier.padding(top = 8.dp),
					otpText = otpValue,
					otpCount = 5,
					onOtpTextChange = { value, isComplete ->
						otpValue = value
						isOtpFilled = isComplete
					}
				)

				if (verificationAttempted && !isOtpFilled) {
					Text(
						text = "O código deve conter exatamente 5 dígitos.",
						style = MaterialTheme.typography.bodySmall,
						color = colorScheme.error,
						modifier = Modifier.padding(top = 6.dp),
					)
				}

				AppButton(
					text = "Verificar Código",
					modifier = Modifier.fillMaxWidth().padding(top = 24.dp).height(52.dp),
					enabled = isOtpFilled,
					onClick = {
						verificationAttempted = true
						if (isOtpFilled) {
							onVerifyClick(otpValue)
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