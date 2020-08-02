import kotlinx.serialization.Serializable

@Serializable
data class CowSay(val render: String) {
    companion object {
        const val path = "/render"
    }
}