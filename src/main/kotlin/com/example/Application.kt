package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

//Host 0.0.0.0 corresponds to the localhost. If you want to access the server only locally,
// you can specify 127.0.0.1 or localhost.

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::mainModule)
        .start(wait = true)
}

fun Application.mainModule() {
    installPlugins()
}