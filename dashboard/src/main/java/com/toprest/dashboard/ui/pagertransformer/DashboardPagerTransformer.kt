package com.toprest.dashboard.ui.pagertransformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

private const val POSITION_LEFT = -1F
private const val POSITION_RIGHT = 1F
private const val POSITION_CENTER = 0F
private const val TRANSPARENT = 0F
private const val OPAQUE = 1F

class DashboardPagerTransformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        when {
            position <= POSITION_LEFT || position >= POSITION_RIGHT -> transformOutside(view, position)
            position == POSITION_CENTER -> transformInCenter(view, position)
            else -> transformInside(view, position)
        }
    }

    private fun transformOutside(view: View, position: Float) {
        view.run {
            translationX = width * position
            alpha = TRANSPARENT
        }
    }

    private fun transformInCenter(view: View, position: Float) {
        view.run {
            translationX = width * position
            alpha = OPAQUE
        }
    }

    private fun transformInside(view: View, position: Float) {
        view.run {
            translationX = width * -position
            alpha = OPAQUE - abs(position)
        }
    }
}
