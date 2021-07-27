package com.toprest.dashboard.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.toprest.commonui.utils.withReference
import com.toprest.commonui.utils.withString
import com.toprest.commonui.utils.withTypedArray
import com.toprest.dashboard.R
import com.toprest.dashboard.databinding.ViewTabButtonBinding

class TabButton : LinearLayout {

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private lateinit var binding :  ViewTabButtonBinding

    private fun init(attrs: AttributeSet?) {
        binding = ViewTabButtonBinding.inflate(LayoutInflater.from(context), this)
        orientation = VERTICAL

        parseAttributes(attrs)
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        attrs?.withTypedArray(context, R.styleable.TabButton) {
            withReference(R.styleable.TabButton_android_src) { binding.icon.setImageDrawable(it) }
            withString(R.styleable.TabButton_android_text) { binding.text.text = it }
        }
    }
}

