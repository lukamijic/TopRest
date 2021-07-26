package com.toprest.coreui.utils

import android.graphics.drawable.Drawable
import android.widget.TextView

private const val DRAWABLE_LEFT_INDEX = 0
private const val DRAWABLE_TOP_INDEX = 1
private const val DRAWABLE_RIGHT_INDEX = 2
private const val DRAWABLE_BOTTOM_INDEX = 3

var TextView.drawableLeft: Drawable?
    get() = compoundDrawables[DRAWABLE_LEFT_INDEX]
    set(drawable) = setDrawablesOrDefault(left = drawable)

var TextView.drawableTop: Drawable?
    get() = compoundDrawables[DRAWABLE_TOP_INDEX]
    set(drawable) = setDrawablesOrDefault(top = drawable)

var TextView.drawableRight: Drawable?
    get() = compoundDrawables[DRAWABLE_RIGHT_INDEX]
    set(drawable) = setDrawablesOrDefault(right = drawable)

var TextView.drawableBottom: Drawable?
    get() = compoundDrawables[DRAWABLE_BOTTOM_INDEX]
    set(drawable) = setDrawablesOrDefault(bottom = drawable)

/**
 * Sets all of the [TextView] drawables at once. If a drawable at a specific spot is not
 * defined, its value will not be changed (its already set value will be used).
 */
fun TextView.setDrawablesOrDefault(
    left: Drawable? = drawableLeft,
    top: Drawable? = drawableTop,
    right: Drawable? = drawableRight,
    bottom: Drawable? = drawableBottom
) = setDrawablesOrNull(left, top, right, bottom)

/**
 * Sets all of the [TextView] drawables at once. If a drawable at a specific spot is not
 * defined, its value will be set to `null` by default.
 */
fun TextView.setDrawablesOrNull(
    left: Drawable? = null,
    top: Drawable? = null,
    right: Drawable? = null,
    bottom: Drawable? = null
) = setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
