import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import io.ktor.routing.route
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

val booksMap: ConcurrentMap<Long, Book> = ConcurrentHashMap()

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        jackson { }
    }
    install(Routing) {
        route("/api/v1"){
            route("/books") {
                getCollectionRoutes()
            }
            route("/book") {
                getSingleRoutes()
            }
        }
    }
}

fun main() {
    embeddedServer(
        Netty,
        8080,
        watchPaths = listOf("Main"),
        module = Application::module
    ).start(wait = true)
}