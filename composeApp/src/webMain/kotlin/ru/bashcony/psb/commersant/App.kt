package ru.bashcony.psb.commersant

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import assistantbutton.AssistantButton
import assistantbutton.style.Typography

@Composable
fun App(
    initialEmail: String = "",
    initialMail: String = ""
) {
    MaterialTheme(
        typography = Typography,
    ) {
        AIAssistantScreen(
            initialEmail = initialEmail,
            initialMail = initialMail
        )
    }
}
