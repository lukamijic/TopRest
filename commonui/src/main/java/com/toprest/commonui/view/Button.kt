package com.toprest.commonui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.toprest.commonui.R
import com.toprest.commonui.databinding.ViewButtonBinding
import com.toprest.commonui.utils.*
import com.toprest.coreui.utils.drawableBottom
import com.toprest.coreui.utils.drawableLeft
import com.toprest.coreui.utils.drawableRight
import com.toprest.coreui.utils.drawableTop

/**
 * CoinMe continue button
 */

private const val DISABLED_ALPHA = 0.3f
private const val ENABLED_ALPHA = 1f

open class Button : ConstraintLayout {
    
    protected lateinit var binding : ViewButtonBinding

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    protected fun init(attrs: AttributeSet?) {
        binding = ViewButtonBinding.inflate(LayoutInflater.from(context), this)
        parseAttributes(attrs)
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        with(binding) {
            attrs?.withTypedArray(context, R.styleable.Button) {
                withBoolean(R.styleable.Button_android_enabled, true) { isEnabled = it }
                withString(R.styleable.Button_android_text) { buttonText.text = it }
                withReference(R.styleable.Button_android_drawableStart) { buttonText.drawableLeft = it }
                withReference(R.styleable.Button_android_drawableTop) { buttonText.drawableTop = it }
                withReference(R.styleable.Button_android_drawableEnd) { buttonText.drawableRight = it }
                withReference(R.styleable.Button_android_drawableBottom) { buttonText.drawableBottom = it }
                withDimensionPixelSize(R.styleable.Button_android_drawablePadding) { buttonText.compoundDrawablePadding = it }
            }
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        alpha = if (enabled) {
            ENABLED_ALPHA
        } else {
            DISABLED_ALPHA
        }
    }

    open fun setText(text: String) {
        binding.buttonText.text = text
    }
}
