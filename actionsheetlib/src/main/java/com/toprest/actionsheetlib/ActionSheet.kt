package com.toprest.actionsheetlib

import android.content.Context
import android.graphics.Outline
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.toprest.actionsheetlib.databinding.ViewActionSheetBinding
import com.toprest.commonui.utils.withTypedArray
import com.toprest.coreui.utils.hide
import com.toprest.coreui.utils.onThrottledClick
import com.toprest.coreui.utils.removePadding
import com.toprest.coreui.utils.show

typealias OnCollapsedListener = () -> Unit

private const val STYLE_ACTION_SHEET = 1
private const val STYLE_DIALOG = 2

private const val BACKGROUND_MAX_ALPHA = 0.6f
private const val TRANSPARENT_ALPHA = 0f
private const val NO_CHILDREN = 0
private const val BOTTOM_SHEET_INDEX = 1

private const val KEY_SUPER_STATE = "SUPER_STATE"
private const val KEY_BACKGROUND_ALPHA = "BACKGROUND_ALPHA"

/**
 * To use [ActionSheet] just add it to your layout and put it's content in the layout as its children.
 */
@Suppress("TooManyFunctions")
class ActionSheet : CoordinatorLayout {

    var onCollapsedListener: OnCollapsedListener? = null
    var onSlideListener: ((View, slideOffset: Float) -> Unit)? = null


    private val binding = ViewActionSheetBinding.inflate(LayoutInflater.from(context), this)
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var maxAlpha = BACKGROUND_MAX_ALPHA
    private var actionSheetColor = 0
    private var actionSheetTransparentColor = 0
    private var isHeightWrapContent = false
    private var hasRoundedTop = false
    private var autoExpand = false
    private var backgroundClickClosingEnabled = false

    private var actionSheetStyle = STYLE_ACTION_SHEET

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet? = null) {
        if (isInEditMode) {
            return
        }

        elevation = resources.getDimension(R.dimen.actionsheet_elevation)
        readAttrs(attrs)

        isClickable = true
        binding.actionSheetContainer.setBackgroundColor(actionSheetColor)
        binding.transparentBackground.setBackgroundColor(actionSheetTransparentColor)

        binding.transparentBackground.onThrottledClick { if (backgroundClickClosingEnabled) close() }

        when (actionSheetStyle) {
            STYLE_ACTION_SHEET -> setActionSheetStyle()
            STYLE_DIALOG -> setDialogStyle()
        }

        if (autoExpand) {
            post { bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED }
        }
    }

    private fun readAttrs(attrs: AttributeSet?) =
        attrs?.withTypedArray(context, R.styleable.ActionSheet) {
            actionSheetColor = getColor(
                R.styleable.ActionSheet_actionSheetColor,
                ContextCompat.getColor(context, R.color.toprest_background)
            )
            actionSheetTransparentColor = getColor(
                R.styleable.ActionSheet_actionSheetBackgroundColor,
                ContextCompat.getColor(context, R.color.actionsheet_overlay)
            )
            isHeightWrapContent = getBoolean(R.styleable.ActionSheet_actionSheetWrapContent, false)
            autoExpand = getBoolean(R.styleable.ActionSheet_actionSheetAutoExpand, false)
            hasRoundedTop = getBoolean(R.styleable.ActionSheet_actionSheetHasRoundedTop, true)
            maxAlpha =
                getFloat(R.styleable.ActionSheet_actionSheetBackgroundMaxAlpha, BACKGROUND_MAX_ALPHA)
            val isBackgroundTransparent =
                getBoolean(R.styleable.ActionSheet_hasTransparentBackground, false)
            if (isBackgroundTransparent) {
                maxAlpha = TRANSPARENT_ALPHA
            }

            getInt(R.styleable.ActionSheet_actionSheetStyle, STYLE_ACTION_SHEET).let { actionSheetStyle = it }
        }

    private fun setActionSheetStyle() {
        if (!hasRoundedTop) {
            return
        }

        with(binding.actionSheetContainer) {
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    resources.getDimension(R.dimen.actionsheet_corner_radius).let {
                        outline.setRoundRect(0, 0, view.width, view.height + it.toInt(), it)
                    }
                }
            }
            clipToOutline = true
        }
    }

    private fun setDialogStyle() {
        binding.viewContainer.removePadding()
        binding.handle.hide()
    }

    override fun onSaveInstanceState() = Bundle().apply {
        putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState())
        putFloat(KEY_BACKGROUND_ALPHA, binding.transparentBackground.alpha)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            binding.transparentBackground.alpha = state.getFloat(KEY_BACKGROUND_ALPHA)
            super.onRestoreInstanceState(state.getParcelable(KEY_SUPER_STATE))
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (isInEditMode) {
            super.addView(child, index, params)
            return
        }
        when (child.id) {
            R.id.action_sheet_container -> {
                super.addView(child, index, params.apply {
                    this.height =
                        if (isHeightWrapContent) ViewGroup.LayoutParams.WRAP_CONTENT else LayoutParams.MATCH_PARENT
                })
            }
            R.id.transparent_background -> super.addView(child, index, params)
            else -> {
                if (binding.viewContainer.childCount > NO_CHILDREN) {
                    throw IllegalStateException("Action sheet layout can only have 1 child")
                }
                binding.viewContainer.addView(child)
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (isInEditMode) {
            return
        }

        binding.transparentBackground.alpha = TRANSPARENT_ALPHA

        bottomSheetBehavior = BottomSheetBehavior.from(getChildAt(BOTTOM_SHEET_INDEX)).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.transparentBackground.alpha = slideOffset * maxAlpha
                    onSlideListener?.invoke(bottomSheet, slideOffset)
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        hide()
                        backgroundClickClosingEnabled = false
                        onCollapsedListener?.invoke()
                    } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        backgroundClickClosingEnabled = true
                    }
                }
            })
        }

        /**
         * If the view shouldn't expand at the start, we need to hide him so the other views can intercept touch events.
         * This has to be done after initializing BottomSheetBehaviour because otherwise BottomSheetBehaviour won't initialize properly and
         * first time expanding won't work as expected
         */
        if (!autoExpand) {
            post {
                hide()
            }
        }
    }

    fun close(): Boolean = if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        true
    } else {
        false
    }

    fun pullUp() {
        post {
            show()
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    fun setHasRoundedCorners(hasRoundedCorners: Boolean) {
        binding.actionSheetContainer.clipToOutline = hasRoundedCorners
    }
}
