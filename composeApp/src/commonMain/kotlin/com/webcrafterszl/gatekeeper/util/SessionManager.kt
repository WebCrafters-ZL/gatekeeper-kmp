package com.webcrafterszl.gatekeeper.util

/**
 * Objeto singleton para gerenciamento de sessão em memória.
 *
 * Este objeto retém o token JWT do usuário após o login, permitindo que ele seja
 * acessado de forma global por toda a aplicação (ViewModels, Repositórios, etc.).
 *
 * ATENÇÃO: Por ser uma implementação em memória, o token será perdido se o
 * aplicativo for completamente encerrado. Para persistência real, seria necessário
 * usar uma solução de armazenamento específica da plataforma (ex: DataStore no Android,
 * Keychain no iOS).
 */
object SessionManager {
    /**
     * O token JWT do usuário autenticado.
     * É nulo (null) antes do login e preenchido após a autenticação bem-sucedida.
     */
    var token: String? = null
}