package com.example.plugins

import io.ktor.server.application.*

// Todo: Consider renaming this to `pluginsModule`
fun Application.installPlugins(){
    configureRouting()
}