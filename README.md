# Gatekeeper (Módulo Frontend KMP)

[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-444444?logo=jetpackcompose&logoColor=white)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![Ktor](https://img.shields.io/badge/Ktor-00A8E8?logo=ktor&logoColor=white)](https://ktor.io/)
[![JWT](https://img.shields.io/badge/JWT-000000?logo=jsonwebtokens&logoColor=white)](https://jwt.io/)

## 1. Nome do Projeto:

**Gatekeeper (Módulo Frontend KMP)**

## 2. Integrantes:

- **Caio Cesar Silva Pena**
- **Leonardo Euripedes da Silva**

## 3. Tema Escolhido:

**Interface Multiplataforma para Sistema de Controle de Acesso corporativo**

## 4. Descrição do Problema Resolvido:

O Gatekeeper soluciona a fragmentação e a falta de mobilidade no gerenciamento de acesso físico corporativo. O frontend provê uma interface multiplataforma e centralizada onde Administradores podem gerenciar a equipe de segurança (Gestores), Gestores gerenciam as catracas/leitores (Pontos de Acesso), as Tags (Credenciais RFID) e os Usuários (Portadores), enquanto os próprios funcionários possuem autoatendimento para consultar seus históricos de entrada. Tudo isso consumindo a API REST oficial do projeto com segurança JWT.

> **Importante:** toda a comunicação com o back-end é realizada por meio da **API Spring Boot** desenvolvida pelo grupo, utilizando `Ktor Client` e autenticação `JWT`.

## 5. Lista de Entidades Implementadas:

- **Gestor (Manager)**
- **Portador (Cardholder)**
- **Credencial RFID**
- **Ponto de Acesso**

As entidades foram implementadas com CRUDs completos, comunicando-se via **Ktor Client** com a API Spring Boot do projeto.

## 6. Instruções para Execução:

- **Para Android:** Abra o projeto no Android Studio/IntelliJ e execute a run configuration `composeApp` (ou rode no terminal: `./gradlew :composeApp:installDebug`).
- **Para Desktop (JVM):** Rode no terminal o comando: `./gradlew :composeApp:run`
- **Para Web (Wasm):** Rode no terminal o comando: `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`

## 7. Variáveis de Ambiente Necessárias:

Não há necessidade de configurar variáveis de ambiente externas ou arquivos `.env` no frontend. A comunicação com a API é resolvida internamente via código, no arquivo `BackendBaseUrl.kt`, que aponta dinamicamente para:

- `http://10.0.2.2:8080/` quando executado no Emulador Android, para acessar o `localhost` da máquina hospedeira;
- `http://localhost:8080/` para os módulos Desktop e Web.

## 8. Exemplos de Usuários/Senhas para Teste:

Para testar a navegação baseada em perfis (**Role-Based Routing**) e a interceptação de JWT, utilize os usuários previamente semeados no banco do back-end:

- **Perfil ADMIN:** `admin@gatekeeper.com` / `admin123`
- **Perfil MANAGER (Gestor):** `manager@gatekeeper.com` / `manager123`
- **Perfil CARDHOLDER (Portador):** `user@gatekeeper.com` / `user123`

## 9. Divisão de Responsabilidades por Integrante:

- **Caio Cesar Silva Pena:** Responsável por toda a estrutura de Navegação por Perfis (JWT Routing) e requisições HTTP base (Ktor + `SessionManager`). Implementou os CRUDs completos de Gestor (`Manager`) e de Portador (`Cardholder`).
- **Leonardo Euripedes da Silva:** Responsável por todo o fluxo de Autenticação (Telas de Login e Redefinição de Senha/Primeiro Acesso). Implementou os CRUDs completos de Credenciais RFID e de Pontos de Acesso (`Access Points`).

---

## Informações adicionais do projeto

### Tecnologias utilizadas

- Kotlin Multiplatform
- Compose Multiplatform
- Material 3
- Ktor Client
- Kotlinx Serialization
- JWT para autenticação e controle de acesso

### Observações arquiteturais

- O frontend foi estruturado para consumir exclusivamente a API REST oficial do Gatekeeper.
- O roteamento de telas é orientado por perfil (`ADMIN`, `MANAGER`, `CARDHOLDER`).
- A camada de rede está centralizada em repositórios no diretório `data/remote/`.
- As telas e componentes visuais seguem o padrão do tema `GatekeeperTheme`.
