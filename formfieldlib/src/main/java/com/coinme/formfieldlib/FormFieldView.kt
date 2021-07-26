package com.coinme.formfieldlib

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import com.toprest.commonui.utils.*
import com.toprest.coreui.utils.addBorderlessRipple
import com.toprest.coreui.utils.onThrottledClick

private const val ICON_POSITION_LEFT = 0
private const val ICON_POSITION_RIGHT = 1
private const val DEFAULT_ICON_POSITION = ICON_POSITION_RIGHT
private const val DEFAULT_ICON_PADDING = 0
private const val EDIT_TEXT_WEIGHT = 1f
private const val DEFAULT_IS_EDITING_ENABLED = true
private const val DEFAULT_LINES = 1

class FormFieldView : FrameLayout {

    private val DEFAULT_TEXT_COLOR: Int = ResourcesCompat.getColor(resources, R.color.font_primary, null)
    private val DEFAULT_ERROR_TEXT_COLOR: Int = ResourcesCompat.getColor(resources, R.color.font_error, null)

    private val SHOW_ICON by lazy { R.drawable.ic_eye_closed }
    private val HIDE_ICON by lazy { R.drawable.ic_eye_open }

    private val DEFAULT_TEXT_SIZE: Int = resources.getDimensionPixelSize(R.dimen.formfieldlib_text_size)
    private val DEFAULT_INPUT_TYPE: Int = EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
    private val PASSWORD_INPUT_TYPE: Int = EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD

    private var textSize = DEFAULT_TEXT_SIZE

    private var textColor = DEFAULT_TEXT_COLOR
    private var errorTextColor = DEFAULT_ERROR_TEXT_COLOR
    private var editTextTypeface: Typeface? = null

    private var inputType: Int = DEFAULT_INPUT_TYPE

    private var activeStateBackground = ResourcesCompat.getDrawable(resources, R.drawable.bg_form_field_active, null)
    private var inactiveStateBackground = ResourcesCompat.getDrawable(resources, R.drawable.bg_form_field_inactive, null)
    private var errorStateBackground = ResourcesCompat.getDrawable(resources, R.drawable.bg_form_field_error, null)

    private var iconDrawable: Drawable? = null
    private var iconPosition: Int = DEFAULT_ICON_POSITION
    private var iconPaddingHorizontal: Int = DEFAULT_ICON_PADDING

    private val editTextPadding by lazy { resources.getDimensionPixelSize(R.dimen.formfieldlib_padding) }
    private val zeroPadding = 0

    private lateinit var editText: AppCompatEditText
    private var iconView: ImageView? = null
    private var isPasswordShown: Boolean = false
    private var showFrame = true
    private var currentBackgroundDrawable = ResourcesCompat.getDrawable(resources, R.drawable.bg_form_field_inactive, null)

    private var maxLines = DEFAULT_LINES
    private var minLines = DEFAULT_LINES

    private var textWatcher: TextWatcher? = null

    var isInErrorState = false
        set(value) {
            field = value
            if (value) {
                enterErrorState()
            } else {
                exitErrorState()
            }
        }

    private var _isEditingEnabled = DEFAULT_IS_EDITING_ENABLED
    var isEditingEnabled: Boolean
        get() = editText.isEnabled
        set(isEnabled) {
            editText.isEnabled = isEnabled
        }

    private val focusChangeListener = OnFocusChangeListener { _, hasFocus ->
        if (!isInErrorState) {
            if (hasFocus) {
                enterActiveState()
            } else {
                enterInactiveState()
            }
        }
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        parseAttributes(attrs)

        setCurrentBackground()

        val linearLayout = LinearLayout(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
        addView(linearLayout)

        if (inputType == PASSWORD_INPUT_TYPE) {
            configurePasswordLayout(linearLayout, attrs)
        } else {
            configureStandardLayout(linearLayout, attrs)
        }

        clipToOutline = true
    }

    fun input() = editText

    fun getText(): String = editText.text.toString()

    fun setText(editFieldText: String) = editText.setText(editFieldText)

    fun addTextWatcher(textWatcher: TextWatcher) {
        editText.addTextChangedListener(textWatcher)
        this.textWatcher = textWatcher
    }

    fun setOnIconClickListener(listener: OnClickListener?) = iconView?.onThrottledClick(listener)

    fun setOnIconClickListener(onClick: () -> Unit) = iconView?.onThrottledClick { onClick() }

    fun showFrame(showFrame: Boolean) {
        this.showFrame = showFrame
        setCurrentBackground()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        editText.isEnabled = false
        val dummyView = View(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            onThrottledClick(l)
        }
        addView(dummyView)
    }

    override fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        editText.onFocusChangeListener = l
    }

    override fun isFocused(): Boolean = editText.isFocused

    private fun setCurrentBackground() {
        background = if (showFrame) currentBackgroundDrawable else null
    }

    private fun enterActiveState() {
        currentBackgroundDrawable = activeStateBackground
        setCurrentBackground()
        editText.setTextColor(textColor)
    }

    private fun enterInactiveState() {
        currentBackgroundDrawable = inactiveStateBackground
        setCurrentBackground()
        editText.setTextColor(textColor)
    }

    private fun enterErrorState() {
        currentBackgroundDrawable = errorStateBackground
        setCurrentBackground()
        editText.setTextColor(errorTextColor)
    }

    private fun exitErrorState() {
        if (editText.hasFocus()) {
            enterActiveState()
        } else {
            enterInactiveState()
        }
    }

    private fun parseAttributes(attrs: AttributeSet?) {
        attrs?.withTypedArray(context, R.styleable.FormFieldView) {
            withDimensionPixelSize(R.styleable.FormFieldView_android_textSize, textSize) { textSize = it }

            withColor(R.styleable.FormFieldView_android_textColor, DEFAULT_TEXT_COLOR) { textColor = it }
            withColor(R.styleable.FormFieldView_errorTextColor, DEFAULT_ERROR_TEXT_COLOR) { errorTextColor = it }
            withString(R.styleable.FormFieldView_android_fontFamily) { editTextTypeface = Typeface.create(it, Typeface.NORMAL) }

            withReference(R.styleable.FormFieldView_activeStateBackground) { activeStateBackground = it }
            withReference(R.styleable.FormFieldView_inactiveStateBackground) { inactiveStateBackground = it }
            withReference(R.styleable.FormFieldView_errorStateBackground) { errorStateBackground = it }

            withInt(R.styleable.FormFieldView_android_inputType) { inputType = it }
            withBoolean(R.styleable.FormFieldView_android_enabled, DEFAULT_IS_EDITING_ENABLED) { _isEditingEnabled = it }

            withReference(R.styleable.FormFieldView_icon) { iconDrawable = it }
            withInt(R.styleable.FormFieldView_iconPosition, ICON_POSITION_RIGHT) { iconPosition = it }
            withDimensionPixelSize(R.styleable.FormFieldView_iconPaddingHorizontal, DEFAULT_ICON_PADDING) { iconPaddingHorizontal = it }
            withInt(R.styleable.FormFieldView_android_maxLines, DEFAULT_LINES) { maxLines = it}
            withInt(R.styleable.FormFieldView_android_minLines, DEFAULT_LINES) { minLines = it}
        }
    }

    private fun createEditText(leftPadding: Int, rightPadding: Int, attrs: AttributeSet?): AppCompatEditText {
        editText = AppCompatEditText(context, attrs).apply {
            layoutParams = LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
                weight = EDIT_TEXT_WEIGHT
            }
            background = null
            setPadding(leftPadding, editTextPadding, rightPadding, editTextPadding)

            editTextTypeface?.let { typeface = it }

            onFocusChangeListener = focusChangeListener

            isEnabled = _isEditingEnabled

            gravity = Gravity.START or Gravity.TOP
        }

        return editText
    }

    private fun createIconView(drawable: Drawable): ImageView {
        iconView = AppCompatImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            setImageDrawable(drawable)
            isClickable = false
            setPadding(iconPaddingHorizontal, DEFAULT_ICON_PADDING, iconPaddingHorizontal, DEFAULT_ICON_PADDING)
        }

        return iconView!!
    }

    private fun createPasswordShowHideButton(): ImageView =
        ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            setPadding(editTextPadding, editTextPadding, editTextPadding, editTextPadding)
            setImageResource(SHOW_ICON)

            addBorderlessRipple()

            onThrottledClick {
                if (isPasswordShown) {
                    setImageResource(SHOW_ICON)
                    editText.withoutTextChangedEvent {
                        transformationMethod = PasswordTransformationMethod.getInstance()
                    }
                    editText.text?.let {
                        editText.setSelection(it.length)
                    }
                } else {
                    setImageResource(HIDE_ICON)
                    editText.withoutTextChangedEvent {
                        transformationMethod = HideReturnsTransformationMethod.getInstance()
                    }
                    editText.text?.let {
                        editText.setSelection(it.length)
                    }
                }

                isPasswordShown = !isPasswordShown
            }
        }

    private fun EditText.withoutTextChangedEvent(block: EditText.() -> Unit) {
        removeTextChangedListener(textWatcher)
        block()
        addTextChangedListener(textWatcher)
    }

    private fun configurePasswordLayout(parentLayout: LinearLayout, attrs: AttributeSet?) {
        parentLayout.addView(createEditText(editTextPadding, zeroPadding, attrs).apply {
            transformationMethod = PasswordTransformationMethod.getInstance()
        })
        parentLayout.addView(createPasswordShowHideButton())
    }

    private fun configureStandardLayout(parentLayout: LinearLayout, attrs: AttributeSet?) {
        iconDrawable?.let {
            when (iconPosition) {
                ICON_POSITION_LEFT -> {
                    parentLayout.addView(createIconView(it))
                    parentLayout.addView(createEditText(zeroPadding, editTextPadding, attrs))
                }
                ICON_POSITION_RIGHT -> {
                    parentLayout.addView(createEditText(editTextPadding, zeroPadding, attrs))
                    parentLayout.addView(createIconView(it))
                }
            }
        } ?: run {
            parentLayout.addView(createEditText(editTextPadding, editTextPadding, attrs))
        }
    }
}
