package com.toprest.commonui.view

import android.content.Context
import android.util.AttributeSet
import com.toprest.commonui.R

class AccentButton : Button {

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    init {
        setBackgroundResource(R.drawable.bg_sandy_brown_rounded)
        binding.buttonText.setTextColor(resources.getColor(R.color.font_white, null))
    }
}
