package com.toprest.commonui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.toprest.commonui.R

class PlateSpinner : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        setBackgroundColor(ContextCompat.getColor(context, R.color.plate_spinner_background))

        inflate(context, R.layout.view_plate_spinner, this)
    }
}
