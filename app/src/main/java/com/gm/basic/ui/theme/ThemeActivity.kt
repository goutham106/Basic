package com.gm.basic.ui.theme

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.LayoutInflaterCompat
import androidx.core.view.isVisible
import com.gm.basic.R
import kotlinx.android.synthetic.main.activity_theme.*
import kotlin.math.hypot

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 2020-01-24.
 */
class ThemeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        LayoutInflaterCompat.setFactory2(
            LayoutInflater.from(this),
            MyLayoutInflater(delegate)
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)

        button.setOnClickListener {
            val newTheme = when (ThemeManager.theme) {
                ThemeManager.Theme.DARK -> ThemeManager.Theme.LIGHT
                ThemeManager.Theme.LIGHT -> ThemeManager.Theme.DARK
            }
            setTheme(newTheme, animate = true)
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        setTheme(ThemeManager.Theme.LIGHT, animate = false)
    }

    private fun setTheme(theme: ThemeManager.Theme, animate: Boolean = true) {
        if (!animate) {
            ThemeManager.theme = theme
            return
        }

        if (imageView.isVisible) {
            return
        }

        val w = container.measuredWidth
        val h = container.measuredHeight

        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        container.draw(canvas)

        imageView.setImageBitmap(bitmap)
        imageView.isVisible = true

        val finalRadius = hypot(w.toFloat(), h.toFloat())

        ThemeManager.theme = theme

        val anim = ViewAnimationUtils.createCircularReveal(container, w / 2, h / 2, 0f, finalRadius)
        anim.duration = 400L
        anim.doOnEnd {
            imageView.setImageDrawable(null)
            imageView.isVisible = false
        }
        anim.start()
    }



    private fun setThemeNew(theme: ThemeManager.Theme, animate: Boolean = true) {
        if (!animate) {
            ThemeManager.theme = theme
            return
        }

        if (imageView.isVisible) {
            return
        }

        val w = container.measuredWidth
        val h = container.measuredHeight

        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        container.draw(canvas)

        imageView.setImageBitmap(bitmap)
        imageView.isVisible = true

        val finalRadius = hypot(w.toFloat(), h.toFloat())

        ThemeManager.theme = theme

        val anim = ViewAnimationUtils.createCircularReveal(imageView, w / 2, h / 2, finalRadius, 0f)
        anim.duration = 400L
        anim.doOnEnd {
            imageView.setImageDrawable(null)
            imageView.isVisible = false
        }
        anim.start()
    }
}










