package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.httpsredirect.*

//Host 0.0.0.0 corresponds to the localhost. If you want to access the server only locally,
// you can specify 127.0.0.1 or localhost.

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

// Todo: Consider renaming this to `mainModule`
fun Application.module() {
    installPlugins()
}
