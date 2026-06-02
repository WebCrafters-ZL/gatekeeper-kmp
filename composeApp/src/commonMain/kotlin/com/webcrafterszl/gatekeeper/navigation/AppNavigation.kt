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
	data object AdminMenu : AppRoute
	data object UserDashboard : AppRoute
	data object PortadorCrud : AppRoute
	data object CredencialCrud : AppRoute
	data object VisitanteCrud : AppRoute
	data object ReservaCrud : AppRoute
	data object GerenciadorConvites : AppRoute
    data object FormularioConvite : AppRoute
    data object HistoricoAcessos : AppRoute
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