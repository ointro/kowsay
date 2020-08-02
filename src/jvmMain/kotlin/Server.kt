import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.github.ricksbrown.cowsay.Cowsay
import org.slf4j.LoggerFactory

//val connectionString: ConnectionString? = System.getenv("MONGODB_URI")?.let {
//    ConnectionString("$it?retryWrites=false")
//}

//val client = if (connectionString != null) KMongo.createClient(connectionString) else KMongo.createClient()
//val database = client.getDatabase(connectionString?.database ?: "test")
//val collection = database.getCollection<ShoppingListItem>()
val collection = mutableListOf<Say>(helloWorld)

fun main() {
    val log = LoggerFactory.getLogger("server")
    val port = System.getenv("PORT")?.toInt() ?: 9090
    embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip()
        }

        routing {
            get("/") {
                call.respondText(
                    this::class.java.classLoader.getResource("index.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            static("/") {
                resources("")
            }
            route(Says.path) {
                get("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: helloWorld.id
                    log.info("get1 ${id} ${collection.size}")

                    val say = collection.find{it.id == id}
                    log.info("get1 ${say} ${collection.map{it.id}.toList()}")
                    call.respond(Says(say ?: helloWorld, collection))
                }
                get {
                    val maybeNewSay = Say(call.request.queryParameters["message"]?.toString() ?: "Hello world")
                    val say = collection.find{it.id == maybeNewSay.id}
                    log.info("get2 ${maybeNewSay} ${say}")
                    if(say == null) {
                        collection.add(0, maybeNewSay)
                    }

                    call.respond(Says(maybeNewSay, collection))
                }

                delete("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    collection.removeIf{it.id == id}
                    call.respond(HttpStatusCode.OK)
                }
            }
            route(Say.path) {
                post {
                    log.info("${Say.path} post")
                    val maybeNewSay = call.receive<Say>() ?: error("Where is Say")

                    collection.add(maybeNewSay)
                    log.info("Adding ${maybeNewSay}")

                    call.respond(Says(maybeNewSay ?: helloWorld, collection))
                }
            }
            route(CowSay.path) {
                get ("/{message}"){
                    val message = call.parameters["message"]?.toString() ?: "42"
                    log.info("Rendering message ${message}")
                    call.respond(Cowsay.say(arrayOf(message)))
                }
            }
        }
    }.start(wait = true)
}