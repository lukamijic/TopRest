package com.toprest.dashboard.view

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.toprest.dashboard.R
import com.toprest.dashboard.databinding.ViewTabBarBinding

private const val HOME_INDEX = 0
private const val SELECTED_TAB_INDEX_KEY = "SELECTED_TAB_INDEX_KEY"
private const val SUPER_STATE_KEY = "SUPER_STATE_KEY"

typealias OnStateChangedListener = (Int) -> Unit
typealias OnTransactButtonClickListener = () -> Unit

class TabBar : ConstraintLayout {

    private var stateListener: OnStateChangedListener? = null
    private val animationDuration by lazy { resources.getInteger(R.integer.tab_bar_indicator_animation_duration) }

    private var binding: ViewTabBarBinding = ViewTabBarBinding.inflate(LayoutInflater.from(context), this)
    private lateinit var tabButtons: List<TabButton>

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        initButtons()
    }

    private fun initButtons() {
        tabButtons = listOf(binding.homeButton, binding.userButton).apply {
            get(HOME_INDEX).isSelected = true

            forEachIndexed { tabIndex, button ->
                button.setOnClickListener { selectTab(tabIndex) }
            }
        }
    }

    private fun selectTab(tabIndex: Int) {
        select(tabIndex)
        stateListener?.invoke(tabIndex)
    }

    fun select(tabIndex: Int) {
        tabButtons.forEachIndexed { index, tabbarButton ->
            tabbarButton.isSelected = index == tabIndex
        }
        moveTabIndicator(tabIndex)
    }

    private fun getSelectedTabIndex() = tabButtons.indexOfFirst { it.isSelected }

    private fun moveTabIndicator(tabIndex: Int) {
        val transition = AutoTransition().apply { duration = animationDuration.toLong() }
        TransitionManager.beginDelayedTransition(this, transition)

        val constraintToButton = tabButtons[tabIndex]
        val params = binding.indicator.layoutParams as LayoutParams
        binding.indicator.layoutParams = params.apply {
            startToStart = constraintToButton.id
            endToEnd = constraintToButton.id
        }
    }

    override fun onSaveInstanceState() = Bundle().apply {
        putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState())
        putInt(SELECTED_TAB_INDEX_KEY, getSelectedTabIndex())
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            selectTab(state.getInt(SELECTED_TAB_INDEX_KEY))
            super.onRestoreInstanceState(state.getParcelable(SUPER_STATE_KEY))
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    fun setOnStateChangedListener(stateChangedListener: OnStateChangedListener?) {
        stateListener = stateChangedListener
    }
}

