package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val users = mapOf(1 to "Ola", 2 to "Uche", 3 to "Victor")

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("/page") {
//            call.application.environment.log.info("\n ${call.receiveText()}")
            try {
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
                // Both parameters are missing
                // returns 404 since no response
            }
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
