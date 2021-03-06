package com.prime.toolz.common.compose

import android.content.res.Resources
import android.graphics.Typeface
import android.text.Spanned
import android.text.style.*
import android.util.Log
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em

/**
 * A composable function that returns the [Resources]. It will be recomposed when [Configuration]
 * gets updated.
 */
@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}


/**
 * Load a quantity string resource.
 *
 * @param id the resource identifier
 * @param quantity The number used to get the correct string for the current language's
 *           plural rules.
 * @return String The string data associated with the resource,
 * stripped of styled text information.
 */
@Composable
@ReadOnlyComposable
fun stringQuantityResource(@PluralsRes id: Int, quantity: Int): String {
    val resources = resources()
    return resources.getQuantityString(id, quantity)
}


/**
 * Return the string value associated with a particular resource ID,
 * substituting the format arguments as defined in {@link java.util.Formatter}
 * and {@link java.lang.String#format}. It will be stripped of any styled text
 * information.
 * {@more}
 *
 * @param id The desired resource identifier, as generated by the aapt
 *           tool. This integer encodes the package, type, and resource
 *           entry. The value 0 is an invalid identifier.
 *
 * @param formatArgs The format arguments that will be used for substitution.
 *
 * @throws NotFoundException Throws NotFoundException if the given ID does not exist.
 *
 * @return String The string data associated with the resource,
 *         stripped of styled text information.
 */
@Composable
@ReadOnlyComposable
fun stringQuantityResource(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any): String {
    val resources = resources()
    return resources.getQuantityString(id, quantity, *formatArgs)
}

@Composable
@ReadOnlyComposable
fun stringHtmlResource(@StringRes id: Int): AnnotatedString {
    val resources = resources()
    val density = LocalDensity.current
    val text = resources.getText(id)
    return if (text !is Spanned) AnnotatedString(text.toString()) else text.annotate(
        density
    )
}

private inline val TypefaceSpan.toSpanStyle: SpanStyle
    get() = SpanStyle(
        fontFamily = when (family) {
            FontFamily.SansSerif.name -> FontFamily.SansSerif
            FontFamily.Serif.name -> FontFamily.Serif
            FontFamily.Monospace.name -> FontFamily.Monospace
            FontFamily.Cursive.name -> FontFamily.Cursive
            else -> FontFamily.Default
        }
    )

private inline val StyleSpan.toSpanStyle: SpanStyle
    get() {
        return when (style) {
            Typeface.NORMAL -> SpanStyle()
            Typeface.BOLD -> SpanStyle(fontWeight = FontWeight.Bold)
            Typeface.ITALIC -> SpanStyle(fontStyle = FontStyle.Italic)
            Typeface.BOLD_ITALIC -> SpanStyle(
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
            else -> error("$style not supported")
        }
    }

/**
 * Helper function for converting [Spanned] to [AnnotatedString]
 */
private fun Spanned.annotate(density: Density) =
    buildAnnotatedString {
        with(density) {
            val text = this@annotate

            // append the string and
            // then apply the supported annotations.
            append((text.toString()))

            // iterate though each span
            // from the old android and
            // return the annotated version of the
            // corresponding span.
            text.getSpans(0, text.length, Any::class.java).forEach { span ->
                val start = text.getSpanStart(span)
                val end = text.getSpanEnd(span)

                val style = when (span) {
                    is StyleSpan -> span.toSpanStyle
                    is TypefaceSpan -> span.toSpanStyle
                    is AbsoluteSizeSpan -> SpanStyle(fontSize = if (span.dip) span.size.dp.toSp() else span.size.toSp())
                    is RelativeSizeSpan -> SpanStyle(fontSize = span.sizeChange.em)
                    is StrikethroughSpan -> SpanStyle(textDecoration = TextDecoration.LineThrough)
                    is UnderlineSpan -> SpanStyle(textDecoration = TextDecoration.Underline)
                    is SuperscriptSpan -> SpanStyle(baselineShift = BaselineShift.Superscript)
                    is SubscriptSpan -> SpanStyle(baselineShift = BaselineShift.Subscript)
                    is ForegroundColorSpan -> SpanStyle(color = Color(span.foregroundColor))
                    // no idea wh this not works with html
                    is BackgroundColorSpan -> SpanStyle(background = Color(span.backgroundColor))
                    else -> /*SpanStyle()*/ error("$span not supported")
                }
                addStyle(style, start, end)
            }
        }
    }

@Composable
@ReadOnlyComposable
fun stringHtmlResource(@StringRes id: Int, vararg formatArgs: Any): AnnotatedString {
    TODO(" Not Implemented Yet!")
}