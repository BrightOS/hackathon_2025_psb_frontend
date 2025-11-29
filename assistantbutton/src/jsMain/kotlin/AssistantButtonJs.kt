package assistantbutton

import androidx.compose.material3.MaterialTheme
import org.jetbrains.compose.web.renderComposable
import kotlinx.browser.document
import kotlinx.browser.window

actual fun assistantNavTo(email: String, mail: String, targetUrl: String) {
    val url = "$targetUrl?email=${js("encodeURIComponent")(email)}&mail=${js("encodeURIComponent")(mail)}"
    window.open(url, "_blank", "noopener,noreferrer")
}

external interface AssistantButtonJsConfig {
    var email: String
    var mail: String
    var targetUrl: String?
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun renderAssistantButton(config: AssistantButtonJsConfig) {
    console.log("renderAssistantButton called with config:", config)

    val containerId = "assistant-button-library-root"
    var container = document.getElementById(containerId)

    if (container == null) {
        console.log("Creating new container")
        container = document.createElement("div")
        container.id = containerId
        document.body?.appendChild(container)
    }

    while (container.firstChild != null) {
        container.removeChild(container.firstChild!!)
    }

    console.log("Rendering AssistantButton to container:", containerId)

    renderComposable(containerId) {
        AssistantButton(
            email = config.email,
            mail = config.mail,
            targetUrl = config.targetUrl ?: "http://localhost:8080/info"
        )
    }

    console.log("AssistantButton rendered successfully")
}
