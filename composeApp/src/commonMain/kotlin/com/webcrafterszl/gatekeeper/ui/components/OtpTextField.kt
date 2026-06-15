package com.webcrafterszl.gatekeeper.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int = 5,
    onOtpTextChange: (String, Boolean) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BasicTextField(
        modifier = modifier.focusRequester(focusRequester),
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= otpCount) {
                onOtpTextChange.invoke(it.text, it.text.length == otpCount)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val availableWidth = maxWidth
                // Calcula um tamanho para as caixas baseado na largura disponível.
                // Subtrai um espaçamento razoável e divide pelo número de inputs.
                // Limita a um tamanho máximo (ex: 60dp) para não ficar gigante em tablets.
                val cellWidth = ((availableWidth - (8.dp * (otpCount - 1))) / otpCount).coerceAtMost(60.dp)
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(otpCount) { index ->
                        CharView(
                            index = index,
                            text = otpText,
                            modifier = Modifier
                                .width(cellWidth)
                                .aspectRatio(1f) // Para manter quadrado, ou ajuste se preferir retângulo
                        )
                        if (index < otpCount - 1) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    val char = when {
        index < text.length -> text[index].toString()
        else -> ""
    }
    val isFocused = text.length == index
    val cellColor = if (isFocused) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
    val borderColor = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(cellColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}