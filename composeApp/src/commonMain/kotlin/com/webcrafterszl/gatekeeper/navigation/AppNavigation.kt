package com.webcrafterszl.gatekeeper.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.webcrafterszl.gatekeeper.data.model.Role

sealed interface AppRoute {
    // Fluxo de Autenticação
    object Login : AppRoute
    object FirstAccessEmail : AppRoute
    data class FirstAccessCode(val email: String) : AppRoute
    data class FirstAccessPassword(val email: String) : AppRoute

    // Dashboards por Perfil
    object AdminDashboard : AppRoute
    object ManagerDashboard : AppRoute
    object CardholderDashboard : AppRoute
    
    // Telas internas (exemplo)
    object FormularioConvite : AppRoute
    object HistoricoAcessos : AppRoute
}

class AppNavigation(initialRoute: AppRoute = AppRoute.Login) {
    var currentRoute by mutableStateOf<AppRoute>(initialRoute)
        private set

    fun navigateTo(route: AppRoute) {
        currentRoute = route
    }

    /**
     * Navega para o dashboard apropriado com base no perfil do usuário,
     * limpando a pilha de navegação para que o usuário não possa voltar para o login.
     * Neste modelo de navegação simples, a "limpeza de pilha" é implícita,
     * pois simplesmente substituímos a rota atual.
     */
    fun navigateToDashboard(role: Role?) {
        currentRoute = when (role) {
            Role.ADMIN -> AppRoute.AdminDashboard
            Role.MANAGER -> AppRoute.ManagerDashboard
            Role.CARDHOLDER -> AppRoute.CardholderDashboard
            else -> AppRoute.Login // Rota de fallback caso o perfil seja nulo/inválido
        }
    }
}