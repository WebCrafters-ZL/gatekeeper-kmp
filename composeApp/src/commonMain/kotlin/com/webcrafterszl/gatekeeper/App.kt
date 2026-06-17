package com.webcrafterszl.gatekeeper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.webcrafterszl.gatekeeper.data.remote.BackendConnectivityProbe
import com.webcrafterszl.gatekeeper.navigation.AppNavigation
import com.webcrafterszl.gatekeeper.navigation.AppRoute
import com.webcrafterszl.gatekeeper.ui.admin.AdminDashboardScreen
import com.webcrafterszl.gatekeeper.ui.auth.FirstAccessCodeScreen
import com.webcrafterszl.gatekeeper.ui.auth.FirstAccessEmailScreen
import com.webcrafterszl.gatekeeper.ui.auth.FirstAccessPasswordScreen
import com.webcrafterszl.gatekeeper.ui.auth.LoginScreen
import com.webcrafterszl.gatekeeper.ui.cardholder.CardholderDashboardScreen
import com.webcrafterszl.gatekeeper.ui.manager.ManagerDashboardScreen
import com.webcrafterszl.gatekeeper.ui.theme.GatekeeperTheme
import com.webcrafterszl.gatekeeper.viewmodel.AdminViewModel
import com.webcrafterszl.gatekeeper.viewmodel.AuthViewModel
import com.webcrafterszl.gatekeeper.viewmodel.CardholderViewModel
import com.webcrafterszl.gatekeeper.viewmodel.ManagerViewModel

@Composable
@Preview
fun App() {
    GatekeeperTheme {
        val navigation = remember { AppNavigation() }
        val backendConnectivityProbe = remember { BackendConnectivityProbe() }
        val isPreview = LocalInspectionMode.current

        // ViewModels que serão utilizados pelas telas
        val authViewModel = viewModel { AuthViewModel() }
        val adminViewModel = viewModel { AdminViewModel() }
        val managerViewModel = viewModel { ManagerViewModel() }
        val cardholderViewModel = viewModel { CardholderViewModel() }

        // Ping no backend para verificar a conectividade ao iniciar o app
        LaunchedEffect(isPreview) {
            if (!isPreview) {
                println(backendConnectivityProbe.ping())
            }
        }

        when (val route = navigation.currentRoute) {
            is AppRoute.Login -> LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { userRole ->
                    // A lógica de decodificação foi movida para o ViewModel.
                    // Aqui, apenas recebemos o Role e navegamos.
                    navigation.navigateToDashboard(userRole)
                },
                onFirstAccessClick = { navigation.navigateTo(AppRoute.FirstAccessEmail) },
            )

            // --- Rotas de Primeiro Acesso ---
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

            // --- Dashboards por Perfil ---
            is AppRoute.AdminDashboard -> AdminDashboardScreen(
                viewModel = adminViewModel,
                onLogoutClick = { navigation.navigateTo(AppRoute.Login) }
            )
            is AppRoute.ManagerDashboard -> ManagerDashboardScreen(
                viewModel = managerViewModel,
                onLogoutClick = { navigation.navigateTo(AppRoute.Login) }
            )
            is AppRoute.CardholderDashboard -> CardholderDashboardScreen(
                viewModel = cardholderViewModel,
                onLogoutClick = { navigation.navigateTo(AppRoute.Login) }
            )
            
            else -> {
                // Rota de fallback
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = { userRole -> navigation.navigateToDashboard(userRole) },
                    onFirstAccessClick = { navigation.navigateTo(AppRoute.FirstAccessEmail) }
                )
            }
        }
    }
}