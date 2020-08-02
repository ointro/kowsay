@file:JsModule("cow-props")
@file:JsNonModule

import react.*

@JsName("default")
external val CW: RClass<CowProps>

external interface CowProps : RProps {
    var message: String
}