package dev.leoesilva.gatekeeper

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform