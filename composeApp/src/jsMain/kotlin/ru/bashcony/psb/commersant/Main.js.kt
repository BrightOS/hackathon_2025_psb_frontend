package ru.bashcony.psb.commersant

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val urlParams = try {
        parseUrlParams(window.location.search)
    } catch (e: Exception) {
        console.log("Error parsing URL params: ${e.message}")
        emptyMap()
    }

    val email = urlParams["email"] ?: ""
    val mail = urlParams["mail"] ?: ""
    val viewtype = urlParams["viewtype"] ?: ""

    console.log("Starting app with email: $email, mail: ${mail.take(50)}, viewtype: $viewtype...")

    ComposeViewport {
        if (viewtype == "")
            App(initialEmail = email, initialMail = mail)
        else
            EmailViewScreen()
    }
}

private fun parseUrlParams(search: String): Map<String, String> {
    if (search.isEmpty() || search == "?") {
        return emptyMap()
    }

    return search
        .removePrefix("?")
        .split("&")
        .mapNotNull { param ->
            val parts = param.split("=", limit = 2)
            if (parts.size == 2) {
                try {
                    parts[0] to decodeURIComponent(parts[1])
                } catch (e: Exception) {
                    console.log("Error decoding param: ${parts[0]}")
                    null
                }
            } else {
                null
            }
        }
        .toMap()
}

private fun decodeURIComponent(encodedURI: String): String {
    return try {
        js("decodeURIComponent(encodedURI)") as String
    } catch (e: Exception) {
        console.log("Error in decodeURIComponent: ${e.message}")
        encodedURI
    }
}
