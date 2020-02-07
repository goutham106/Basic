package com.gm.basic.ui.theme

import androidx.annotation.ColorRes

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 2020-01-24.
 */
object ThemeManager {

    private val listeners = mutableSetOf<ThemeChangedListener>()
    var theme = Theme.LIGHT
        set(value) {
            field = value
            listeners.forEach { listener -> listener.onThemeChanged(value) }
        }

    interface ThemeChangedListener {

        fun onThemeChanged(theme: Theme)
    }

    data class ButtonTheme(
        @ColorRes
        val backgroundTint: Int,
        @ColorRes
        val textColor: Int
    )

    data class TextViewTheme(
        @ColorRes
        val textColor: Int
    )

    data class ViewGroupTheme(
        @ColorRes
        val backgroundColor: Int
    )

    enum class Theme(
        val buttonTheme: ButtonTheme,
        val textViewTheme: TextViewTheme,
        val viewGroupTheme: ViewGroupTheme
    ) {
        DARK(
            buttonTheme = ButtonTheme(
                backgroundTint = android.R.color.holo_green_dark,
                textColor = android.R.color.white
            ),
            textViewTheme = TextViewTheme(
                textColor = android.R.color.white
            ),
            viewGroupTheme = ViewGroupTheme(
                backgroundColor = android.R.color.background_dark
            )
        ),
        LIGHT(
            buttonTheme = ButtonTheme(
                backgroundTint = android.R.color.holo_green_light,
                textColor = android.R.color.black
            ),
            textViewTheme = TextViewTheme(
                textColor = android.R.color.black
            ),
            viewGroupTheme = ViewGroupTheme(
                backgroundColor = android.R.color.background_light
            )
        )
    }

    fun addListener(listener: ThemeChangedListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: ThemeChangedListener) {
        listeners.remove(listener)
    }
}