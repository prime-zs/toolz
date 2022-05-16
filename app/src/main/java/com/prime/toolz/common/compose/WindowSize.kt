package com.prime.toolz.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * A ScaleFactor to multiply the original size for current Box.
 */
val LocalScaleMultiplierProvider = staticCompositionLocalOf<Float> {
    error("No Size/ScaleFactorProvider defined!!")
}

@Stable
inline val Float.sdp: Dp
    @Composable
    get() {
        val multiplier = LocalScaleMultiplierProvider.current
        return Dp(this * multiplier)
    }

@Stable
inline val Int.sdp: Dp
    @Composable
    get() = toFloat().sdp

@Stable
inline val Double.sdp: Dp
    @Composable
    get() = toFloat().sdp

/**
 * @see [Float.sdp]
 */
@Stable
inline val Float.ssp: TextUnit
    @Composable
    get() {
        val multiplier = LocalScaleMultiplierProvider.current

        // return the value scaled according to multiplier.
        return (this * multiplier).sp
    }

/**
 * Creates a SSP unit [TextUnit]
 */
@Stable
val Double.ssp: TextUnit
    @Composable
    get() = toFloat().ssp


/**
 * Creates a SSP unit [TextUnit]
 */
@Stable
val Int.ssp: TextUnit
    @Composable
    get() = toFloat().ssp