package ru.bashcony.psb.commersant

import kotlinx.browser.window

actual fun copyToClipboard(what: String) {
    window.navigator.clipboard.writeText(what)
}