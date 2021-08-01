package com.toprest.home.ui.filter

import android.os.Bundle
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.utils.doOnAnimationEnd
import com.toprest.coreui.utils.onClick
import com.toprest.coreui.utils.onThrottledClick
import com.toprest.coreui.utils.show
import com.toprest.home.databinding.FragmentFilterHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val FILTER_PULLEY_ANIMATION_TIME = 400L
private const val FADE_IN_ALPHA = 0.4f
private const val FADE_OUT_ALPHA = 0f

class FilterFragment : BaseFragment<FilterViewState, FragmentFilterHomeBinding>(FragmentFilterHomeBinding::inflate) {

    companion object {
        const val TAG = "FilterFragment"

        fun newInstance() = FilterFragment()
    }

    override val model : FilterViewModel by viewModel()

    private val stars by lazy { with(binding) { listOf(star1, star2, star3, star4, star5) } }

    override fun FragmentFilterHomeBinding.initialiseView(savedInstanceState: Bundle?) {
        root.post {
            initPulleyPosition()
            animatePulleySlideInFromTop()
            animateBackgroundFadeIn()
        }

        filterPulleyBackground.onThrottledClick {
            animateBackgroundFadeOut()
            animatePulleySlideOutToTop()
        }

        initStars()
        clearFilter.onClick { model.setMinimumScore(0) }
    }

    private fun FragmentFilterHomeBinding.initStars() {
        star1.onClick { model.setMinimumScore(1) }
        star2.onClick { model.setMinimumScore(2) }
        star3.onClick { model.setMinimumScore(3) }
        star4.onClick { model.setMinimumScore(4) }
        star5.onClick { model.setMinimumScore(5) }
    }

    private fun FragmentFilterHomeBinding.initPulleyPosition() {
        filterPulley.translationY = -filterPulley.height.toFloat()
        filterPulley.show()
    }

    private fun FragmentFilterHomeBinding.animatePulleySlideInFromTop() {
        filterPulley
            .animate()
            .translationYBy(filterPulley.height.toFloat())
            .setDuration(FILTER_PULLEY_ANIMATION_TIME)
            .start()
    }

    private fun FragmentFilterHomeBinding.animatePulleySlideOutToTop() {
        filterPulley
            .animate()
            .translationYBy(-filterPulley.height.toFloat())
            .setDuration(FILTER_PULLEY_ANIMATION_TIME)
            .doOnAnimationEnd { model.close() }
            .start()
    }

    private fun FragmentFilterHomeBinding.animateBackgroundFadeIn() {
        filterPulleyBackground
            .animate()
            .alpha(FADE_IN_ALPHA)
            .setDuration(FILTER_PULLEY_ANIMATION_TIME)
            .start()
    }

    private fun FragmentFilterHomeBinding.animateBackgroundFadeOut() {
        filterPulleyBackground
            .animate()
            .alpha(FADE_OUT_ALPHA)
            .setDuration(FILTER_PULLEY_ANIMATION_TIME)
            .start()
    }

    override fun render(viewState: FilterViewState) {
        for (i in stars.indices) {
            stars[i].isSelected = i + 1 <= viewState.minimumScore
        }
    }

    override fun back(): Boolean {
        binding.animateBackgroundFadeOut()
        binding.animatePulleySlideOutToTop()
        return true
    }
}
