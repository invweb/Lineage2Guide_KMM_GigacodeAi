package com.zx_tole.lineage2_guide.server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.core.*
import com.zx_tole.lineage2_guide.server.routing.apiRoutes
import com.zx_tole.lineage2_guide.server.routing.iconRoutes

fun Application.module() {
    install(CORS) {
        allowHost("localhost")
        allowHost("127.0.0.1")
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Options)
    }

    routing {
        get("/") {
            call.respondText(
                """{"service":"Lineage 2 Guide API","version":"1.0.0","status":"running"}""",
                ContentType.Application.Json
            )
        }

        apiRoutes()
        iconRoutes()
    }
}
