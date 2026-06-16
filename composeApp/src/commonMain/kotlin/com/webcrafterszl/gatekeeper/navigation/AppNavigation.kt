package com.webcrafterszl.gatekeeper.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed interface AppRoute {
	data object Login : AppRoute
	data object FirstAccessEmail : AppRoute
	data class FirstAccessCode(val email: String) : AppRoute
	data class FirstAccessPassword(val email: String) : AppRoute
	data object Selection : AppRoute
	data object UserDashboard : AppRoute
    data object FormularioConvite : AppRoute
    data object HistoricoAcessos : AppRoute
    
    // Novas rotas para o módulo do Gestor (Manager)
    data object ManagerMenu : AppRoute
    data object ManagerAccessLogs : AppRoute
    data object ManagerAccessPoints : AppRoute
    data object ManagerCardholders : AppRoute
}

class AppNavigation(initialRoute: AppRoute = AppRoute.Login) {
	var currentRoute by mutableStateOf<AppRoute>(initialRoute)
		private set

	fun navigateTo(route: AppRoute) {
		currentRoute = route
	}

	fun goBackToSelection() {
		currentRoute = AppRoute.Selection
	}
}