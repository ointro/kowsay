import react.*
import react.dom.*
import kotlinext.js.*
import kotlinx.html.js.*
import kotlinx.coroutines.*
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.id

val Cow: (props: CowProps) -> dynamic = functionalComponent<CowProps> { props ->
    val (cowSay, setCowSay) = useState(CowSay("Cow says Hi!"))

    useEffect(dependencies = listOf(props.message)) {
        scope.launch {
            setCowSay(render(props.message))
        }
    }

//    useDebugValue(cowSay)

    div("cowsay") {
        pre {
            +cowSay.render
        }
    }

//    ul {
//        shoppingList.sortedByDescending(Say::priority).forEach { item ->
//            li {
//                key = item.toString()
//                attrs.onClickFunction = {
//                    scope.launch {
//                        deleteSay(item)
//                        setSayings(getSay())
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
//                    setSayings(getSay())
//                }
//            }
//        }
//    )
}