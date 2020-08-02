import react.*

import react.dom.*
import kotlinext.js.*
import kotlinx.html.js.*
import kotlinx.coroutines.*
import kotlinx.html.ButtonType
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.id
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.url.URL
import org.w3c.dom.url.URLSearchParams
import kotlin.browser.window

val scope = MainScope()

val App = functionalComponent<RProps> { _ ->
    val (say, setSay) = useState(Says(helloWorld, listOf(helloWorld)))
    val (text, setText) = useState("")

    val id = URLSearchParams(window.location.search).get("id")?.toInt()

    val submitHandler: (Event) -> Unit = {
        it.preventDefault()

        scope.launch {
            val s = addSay(Say(text))
            val url = URL(window.location.href)
            val params = URLSearchParams(window.location.search)
            console.log(params)
            if(params.has("id")) {
                console.log("hasId")
                params.set("id", s.display.id.toString())
            } else {
                console.log("noId")
                params.append("id", s.display.id.toString())
            }
            console.log("Replacing")
            console.log(params.toString())
            url.search = params.toString()
            console.log(url.toString())
            window.history.replaceState(jsObject(), "",  url.toString())

            setSay(s)
        }

    }

    val changeHandler: (Event) -> Unit = {
        val value = (it.target as HTMLInputElement).value
        setText(value)
    }

    useEffect(dependencies = listOf()) {
        scope.launch {
            val s = getSay(id ?: say.display.id)

            setSay(s)
        }
    }

//    @JsModule
//    @JsNonModule
//    useDebugValue(say)

    h1 {
        +"Kowsay"
    }

    childList.add(
        Cow(jsObject {
            message = say.display.text
        })
    )

    form(classes = "ui form") {
        attrs.action = "/says"
        attrs.method = FormMethod.post
        attrs.onSubmitFunction = submitHandler

        div(classes="field") {
            label {
                attrs.htmlFor = "message"
                +"Message"


            }
            input {
                attrs.placeholder = "Message"
                attrs.onChangeFunction = changeHandler
                attrs.type = InputType.text
                attrs.name = "message"
                attrs.id = "message"
                attrs.required = true
            }
        }

        button(classes = "ui button", type = ButtonType.submit) {
            attrs.value = "Display"
            +"Display"
        }
    }

//    ul {
//        say.sortedByDescending(Say::priority).forEach { item ->
//            li {
//                key = item.toString()
//                attrs.onClickFunction = {
//                    scope.launch {
//                        deleteSay(item)
//                        setSay(getSay())
//                    }
//                }
//                +"[${item.priority}] ${item.text} "
//            }
//        }
//    }
//    child(
//        InputComponent,
//        props = jsObject {
//            onSubmit = { input ->
//                val cartItem = Say(input.replace("!", ""), input.count { it == '!' })
//                scope.launch {
//                    addSay(cartItem)
//                    setSay(getSay())
//                }
//            }
//        }
//    )
}