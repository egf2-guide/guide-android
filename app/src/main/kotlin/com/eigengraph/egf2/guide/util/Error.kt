package com.eigengraph.egf2.guide.util

fun parseError(message: String): String {
    if (message.contains("com.google.gson", true))
        return "Error parsing models, please contact backend"
    return message
}
