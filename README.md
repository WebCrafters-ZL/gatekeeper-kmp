# GateKeeper

Aplicacao Kotlin Multiplatform para controle de acesso, com fluxos de login/seleção e CRUDs conectados ao Firebase Realtime Database.

## Visao geral

O projeto centraliza quatro entidades principais:

- Portador
- Credencial RFID
- Visitante
- Reserva

A navegacao principal passa por:

`Login -> Cadastro -> Selecao -> Menus (Administrativo/Autoatendimento) -> CRUDs`

## Equipe

- Caio Cesar Silva Pena ([@caiocesardev](https://github.com/caiocesardev))
- Leonardo Euripedes da Silva ([@leoesilva](https://github.com/leoesilva))

## Stack tecnica

- Kotlin Multiplatform
- Compose Multiplatform (UI)
- Material 3
- Ktor Client (HTTP)
- Kotlinx Serialization (JSON)
- Firebase Realtime Database (persistencia remota)

## Estrutura do projeto

- `composeApp/src/commonMain/kotlin`: regras compartilhadas, telas, navegacao, viewmodels e repositorio remoto
- `composeApp/src/androidMain`: codigo especifico Android
- `composeApp/src/jvmMain`: codigo especifico Desktop (JVM)
- `composeApp/src/jsMain`: codigo especifico Web (JS)
- `composeApp/src/wasmJsMain`: codigo especifico WebAssembly

## Executar localmente

Use os comandos abaixo na raiz do projeto.

### Android (build debug)

```bash
./gradlew :composeApp:assembleDebug
```

### Desktop (JVM)

```bash
./gradlew :composeApp:run
```

### Web (Wasm)

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

### Web (JS)

```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

## Arquivos-chave

- `composeApp/src/commonMain/kotlin/com/webcrafterszl/gatekeeper/App.kt`: ponto de entrada da UI e roteamento de telas
- `composeApp/src/commonMain/kotlin/com/webcrafterszl/gatekeeper/navigation/AppNavigation.kt`: estado de navegacao
- `composeApp/src/commonMain/kotlin/com/webcrafterszl/gatekeeper/ui/screens/CrudScreens.kt`: telas de CRUD
- `composeApp/src/commonMain/kotlin/com/webcrafterszl/gatekeeper/data/remote/RepositorioRemoto.kt`: integracao com Firebase Realtime Database
