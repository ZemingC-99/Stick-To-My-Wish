package com.example.StickToMyWish.util

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat


class ImageUtil {

    fun tintDrawable(drawable: Drawable?, colors: Int): Drawable? {
        val wrappedDrawable = DrawableCompat.wrap(drawable!!).mutate()
        DrawableCompat.setTint(wrappedDrawable, colors)
        return wrappedDrawable
    }

}