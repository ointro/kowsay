import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

import kotlin.browser.window

val endpoint = window.location.origin // only needed until https://github.com/ktorio/ktor/issues/1695 is resolved

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun render(message: String): CowSay {
    return CowSay(
        jsonClient.get(endpoint+CowSay.path+"/${message}")
    )
}

suspend fun getSay(id: Int): Says {
    return jsonClient.get(endpoint + Says.path+ "/${id}")
}

suspend fun addSay(say: Say): Says {
    return jsonClient.post<Says>(endpoint + Say.path) {
        contentType(ContentType.Application.Json)
        body = say
    }
}

suspend fun deleteSay(say: Say) {
    jsonClient.delete<Unit>(endpoint + Say.path + "/${say.id}")
}