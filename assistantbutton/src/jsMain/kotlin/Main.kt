package assistantbutton

import kotlinx.browser.window

fun main() {
    console.log("AssistantButton library loaded!")

    window.asDynamic().renderAssistantButton = ::renderAssistantButton

    console.log("✓ window.renderAssistantButton is ready")
    console.log("Type: ", js("typeof window.renderAssistantButton"))
}
