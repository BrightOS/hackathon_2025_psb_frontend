package assistantbutton

import android.content.Context
import android.content.Intent
import android.net.Uri
private var appContext: Context? = null

fun initAssistantButton(context: Context) {
    appContext = context.applicationContext
}

internal actual fun assistantNavToInternal(email: String, mail: String, targetUrl: String) {
    val context = appContext ?: return
    val url = "$targetUrl?email=${Uri.encode(email)}&mail=${Uri.encode(mail)}"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}
