package com.example.plugins

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {
    val users = mapOf(1 to "Ola", 2 to "Uche", 3 to "Victor")

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("/page") {
            // Receive only text parameters
            /*try {
                // POST body parameters
                val formParameters = call.receiveParameters()
                val name = formParameters["name"]
                val lastName = formParameters["lastname"]
                name?.let {
                    lastName?.let {
                        call.respondText("Hello, $name $lastName!")
                    }
                }
            } catch (e: ContentTransformationException) {
                // Some parameters are missing
                // returns 404 since no response
            }*/

            // Full access to all parameters including files using Multipart form data
            try {
                val parts = call.receiveMultipart().readAllParts()
                parts.forEach { partData ->
                    when (partData) {
                        // If it is text parameter
                        is PartData.FormItem -> {
                            call.application.environment.log.info("${partData.name} : ${partData.value}")
                        }
                        // If it is file parameter
                        is PartData.FileItem -> {
                            val fileName = partData.originalFileName
                            val fileBytes = partData.streamProvider().readBytes()
                            val pathName = "avatars/$fileName"
                            // Create a new file in the /avatars folder with the received name and content (bytes)
                            File(pathName).writeBytes(fileBytes)
                        }

                        else -> Unit
                    }
                }
            } catch (e: Exception){
                call.application.environment.log.error(e.toString())
                // Some parameters are missing
                // returns 404 since no response
            }

            // Getting Headers
            call.request.headers.forEach { title, data -> call.application.environment.log.info("$title : ${data[0]}") }
            // User-Agent (devices and browsers from which users access the site.)
            call.application.environment.log.info(call.request.header("User-Agent").toString())
            // Cookie Header
            // A particular cookie
            call.application.environment.log.info(call.request.cookies["Cookie_1"])
            call.application.environment.log.info(call.request.headers["Cookie"])

            // Getting Http method, host, and uri
            call.application.environment.log.info(call.request.httpMethod.toString())
            call.application.environment.log.info(call.request.host())
            call.application.environment.log.info(call.request.uri)
        }

        route("/main") {
            get {
                call.respond("This is the main page")
            }
            // Using Wildcard
            get("/*") {
                call.respondText(call.request.path())
            }
            // Using Tailcard
            get("/{...}") {
                call.respondText(call.request.path())
            }
            get("/login") {
                call.respond("Please, login!")
            }
            post { }
            head { }
            options { }
        }

        route("/resources") {
//            get {
//                call.respond(users.values.joinToString())
//            }
            get {
                // GET query parameters
                val queryParameters = call.request.queryParameters
                if (queryParameters.isEmpty()) {
                    call.respond(users.values.joinToString())
                } else {
                    queryParameters["id"]?.let { stringId ->
                        stringId.toIntOrNull()?.let { id ->
                            users[id]?.let { user ->
                                call.respondText(user)
                            }
                        }
                    }
                }

            }
            // GET path parameter, id
            get("/{id}") {
                call.parameters["id"]?.let { stringId ->
                    stringId.toIntOrNull()?.let { id ->
                        users[id]?.let { user ->
                            call.respondText(user)
                        }
                    }
                }
            }
        }
    }
}
