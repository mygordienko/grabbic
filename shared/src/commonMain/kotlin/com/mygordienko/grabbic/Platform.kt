package com.mygordienko.grabbic

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform