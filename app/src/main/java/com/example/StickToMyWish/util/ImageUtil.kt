package com.example.StickToMyWish.util

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat


class ImageUtil {
    // Tints a given drawable with the specified color.
    // Returns the tinted drawable or null if the input drawable is null.
    // 'colors' parameter should be a color integer.
    fun tintDrawable(drawable: Drawable?, colors: Int): Drawable? {
        val wrappedDrawable = DrawableCompat.wrap(drawable!!).mutate()
        DrawableCompat.setTint(wrappedDrawable, colors)
        return wrappedDrawable
    }

}