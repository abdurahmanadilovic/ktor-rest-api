import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*


fun Route.getCollectionRoutes() {
    get {
        call.respond(synchronized(booksMap) { booksMap.values })
    }
}

fun Route.getSingleRoutes() {
    post {
        val book = call.receive<Book>()
        booksMap[book.isbn] = book
        call.respond(book)
    }
    route("/{id}") {
        get {
            booksMap.getOrDefault(
                call.parameters["id"]?.toLongOrNull(),
                null
            )?.let {
                call.respond(it)
            }

            call.respond(mapOf("error" to "Not found"))
        }

        delete {
            call.parameters["id"]?.toLongOrNull()?.let { id ->
                booksMap.remove(id)?.let {
                    call.respond(id)
                }
                call.respond(mapOf("error" to "Not found!"))
            }
            call.respond(mapOf("error" to "Id is not of type Long!"))
        }

        put {
            call.parameters["id"]?.toLongOrNull()?.let { id ->
                booksMap[id]?.let {
                    val updated = call.receive<Book>()
                    booksMap[id] = updated
                    call.respond(updated)
                }
                call.respond(mapOf("error" to "Not found!"))
            }
            call.respond(mapOf("error" to "Id is not of type Long!"))
        }
    }
}
