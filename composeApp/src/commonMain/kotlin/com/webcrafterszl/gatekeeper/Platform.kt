package com.webcrafterszl.gatekeeper

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform