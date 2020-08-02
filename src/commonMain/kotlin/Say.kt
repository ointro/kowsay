import kotlinx.serialization.Serializable

@Serializable
data class Say(val text: String) {
    val id: Int = text.hashCode()

    companion object {
        const val path = "/say"
    }
}

@Serializable
data class Says(val display: Say, val all: List<Say>) {
    companion object {
        const val path = "/says"
    }

}

val helloWorld = Say("Hello world")
