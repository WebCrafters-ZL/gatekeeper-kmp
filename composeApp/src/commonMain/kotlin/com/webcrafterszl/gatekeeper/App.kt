package com.webcrafterszl.gatekeeper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.webcrafterszl.gatekeeper.data.remote.BackendConnectivityProbe
import com.webcrafterszl.gatekeeper.navigation.AppNavigation
import com.webcrafterszl.gatekeeper.navigation.AppRoute
import com.webcrafterszl.gatekeeper.ui.auth.FirstAccessCodeScreen
import com.webcrafterszl.gatekeeper.ui.auth.FirstAccessEmailScreen
import com.webcrafterszl.gatekeeper.ui.auth.FirstAccessPasswordScreen
import com.webcrafterszl.gatekeeper.ui.auth.LoginScreen
import com.webcrafterszl.gatekeeper.ui.auth.SelectionScreen
import com.webcrafterszl.gatekeeper.ui.components.AppButton
import com.webcrafterszl.gatekeeper.ui.manager.AccessLogScreen
import com.webcrafterszl.gatekeeper.ui.manager.AccessPointScreen
import com.webcrafterszl.gatekeeper.ui.manager.CardholderScreen
import com.webcrafterszl.gatekeeper.ui.user.HistoricoAcessosScreen
import com.webcrafterszl.gatekeeper.ui.user.UserDashboardScreen
import com.webcrafterszl.gatekeeper.ui.user.visitantes.FormularioConviteScreen
import com.webcrafterszl.gatekeeper.ui.theme.GatekeeperTheme
import com.webcrafterszl.gatekeeper.viewmodel.FormularioConviteViewModel

@Composable
@Preview
fun App() {
    GatekeeperTheme {
        val navigation = remember { AppNavigation() }
        val backendConnectivityProbe = remember { BackendConnectivityProbe() }
        val isPreview = LocalInspectionMode.current

        LaunchedEffect(isPreview) {
            if (!isPreview) {
                println(backendConnectivityProbe.ping())
            }
        }

        val formularioConviteViewModel = remember { FormularioConviteViewModel() }

        when (val route = navigation.currentRoute) {
            is AppRoute.Login -> LoginScreen(
                onLoginSuccess = { _ ->
                    navigation.navigateTo(AppRoute.Selection) 
                },
                onFirstAccessClick = { navigation.navigateTo(AppRoute.FirstAccessEmail) },
            )

            is AppRoute.FirstAccessEmail -> FirstAccessEmailScreen(
                onNextClick = { email -> navigation.navigateTo(AppRoute.FirstAccessCode(email)) },
                onBackClick = { navigation.navigateTo(AppRoute.Login) },
            )

            is AppRoute.FirstAccessCode -> FirstAccessCodeScreen(
                email = route.email,
                onVerifyClick = { _ -> navigation.navigateTo(AppRoute.FirstAccessPassword(route.email)) },
                onBackClick = { navigation.navigateTo(AppRoute.FirstAccessEmail) },
            )

            is AppRoute.FirstAccessPassword -> FirstAccessPasswordScreen(
                email = route.email,
                onSetPasswordClick = { _ -> navigation.navigateTo(AppRoute.Login) },
                onBackClick = { navigation.navigateTo(AppRoute.FirstAccessCode(route.email)) },
            )

            is AppRoute.Selection -> SelectionScreen(
                onAdminClick = { navigation.navigateTo(AppRoute.ManagerMenu) },
                onUserClick = { navigation.navigateTo(AppRoute.UserDashboard) },
                onLogoutClick = { navigation.navigateTo(AppRoute.Login) },
            )

            is AppRoute.UserDashboard -> UserDashboardScreen(
                onConvidarVisitanteClick = { navigation.navigateTo(AppRoute.FormularioConvite) },
                onHistoricoAcessosClick = { navigation.navigateTo(AppRoute.HistoricoAcessos) },
                onLogoutClick = { navigation.navigateTo(AppRoute.Login) }
            )

            is AppRoute.FormularioConvite -> FormularioConviteScreen(
                viewModel = formularioConviteViewModel,
                onBack = { navigation.navigateTo(AppRoute.UserDashboard) }
            )

            is AppRoute.HistoricoAcessos -> HistoricoAcessosScreen(
                onBack = { navigation.navigateTo(AppRoute.UserDashboard) }
            )
            
            is AppRoute.ManagerMenu -> MenuScreen(
                title = "Painel do Gestor",
                primaryLabel = "Dashboard de Acessos",
                onPrimary = { navigation.navigateTo(AppRoute.ManagerAccessLogs) },
                secondaryLabel = "Pontos de Acesso",
                onSecondary = { navigation.navigateTo(AppRoute.ManagerAccessPoints) },
                tertiaryLabel = "Portadores",
                onTertiary = { navigation.navigateTo(AppRoute.ManagerCardholders) },
                onBack = { navigation.navigateTo(AppRoute.Selection) },
            )

            is AppRoute.ManagerAccessLogs -> AccessLogScreen(
                onBack = { navigation.navigateTo(AppRoute.ManagerMenu) }
            )

            is AppRoute.ManagerAccessPoints -> AccessPointScreen(
                onBack = { navigation.navigateTo(AppRoute.ManagerMenu) }
            )

            is AppRoute.ManagerCardholders -> CardholderScreen(
                onBack = { navigation.navigateTo(AppRoute.ManagerMenu) }
            )
            
            else -> {
                Column(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Rota não encontrada ou em desenvolvimento.", style = MaterialTheme.typography.headlineSmall)
                    AppButton(text = "Voltar para o Login", modifier = Modifier.padding(top = 24.dp), onClick = { navigation.navigateTo(AppRoute.Login) })
                }
            }
        }
    }
}

@Composable
private fun MenuScreen(
    title: String,
    primaryLabel: String,
    onPrimary: () -> Unit,
    secondaryLabel: String,
    onSecondary: () -> Unit,
    tertiaryLabel: String? = null,
    onTertiary: (() -> Unit)? = null,
    onBack: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier.fillMaxSize().background(colorScheme.background).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium, color = colorScheme.tertiary)
        AppButton(text = primaryLabel, modifier = Modifier.padding(top = 24.dp), onClick = onPrimary)
        AppButton(text = secondaryLabel, modifier = Modifier.padding(top = 12.dp), onClick = onSecondary)
        if (tertiaryLabel != null && onTertiary != null) {
            AppButton(text = tertiaryLabel, modifier = Modifier.padding(top = 12.dp), onClick = onTertiary)
        }
        AppButton(text = "Voltar", modifier = Modifier.padding(top = 24.dp), onClick = onBack)
    }
}