package com.bitwarden.authenticator.ui.platform.base.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import java.text.Normalizer
import kotlin.math.floor

/**
 * Returns a new [String] that includes line breaks after [widthPx] worth of text. This is useful
 * for long values that need to smoothly flow onto the next line without the OS inserting line
 * breaks earlier at special characters.
 *
 * Note that the internal calculation used assumes that [monospacedTextStyle] is based on a
 * monospaced font like Roboto Mono.
 */
@Composable
fun String.withLineBreaksAtWidth(
    widthPx: Float,
    monospacedTextStyle: TextStyle,
): String {
    val measurer = rememberTextMeasurer()
    return remember(this, widthPx, monospacedTextStyle) {
        val characterSizePx = measurer
            .measure("*", monospacedTextStyle)
            .size
            .width
        val perLineCharacterLimit = floor(widthPx / characterSizePx).toInt()
        if (widthPx > 0) {
            this
                .chunked(perLineCharacterLimit)
                .joinToString(separator = "\n")
        } else {
            this
        }
    }
}

/**
 * Returns the [String] as an [AnnotatedString].
 */
fun String.toAnnotatedString(): AnnotatedString = AnnotatedString(text = this)

/**
 * Normalizes the [String] by removing diacritics, such as an umlaut.
 *
 * Example: áéíóů --> aeiou
 */
fun String.removeDiacritics(): String =
    "\\p{InCombiningDiacriticalMarks}+"
        .toRegex()
        .replace(
            Normalizer.normalize(this, Normalizer.Form.NFKD),
            "",
        )