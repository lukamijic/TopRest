package com.toprest.navigation.ext

import androidx.fragment.app.FragmentTransaction
import com.toprest.navigation.R

fun FragmentTransaction.applyFadeInEnterAndFadeOutExitAnimation() {
    setCustomAnimations(
        R.anim.fragment_fade_in_enter,
        R.anim.fragment_fade_out_exit,
        R.anim.fragment_fade_in_enter,
        R.anim.fragment_fade_out_exit
    )
}

fun FragmentTransaction.applyShortFadeInEnterAndFadeOutExitAnimation() {
    setCustomAnimations(
        R.anim.fragment_fade_in_enter_short,
        R.anim.fragment_fade_out_exit_short,
        R.anim.fragment_fade_in_enter_short,
        R.anim.fragment_fade_out_exit_short
    )
}

fun FragmentTransaction.applyOpenEnterAndFadeOutExitAnimation() {
    setCustomAnimations(
        R.anim.fragment_open_enter,
        R.anim.fragment_no_animation,
        R.anim.fragment_no_animation,
        R.anim.fragment_fade_out_exit
    )
}

fun FragmentTransaction.applySlideInEnterSlideOutExitAnimation() {
    setCustomAnimations(
        R.anim.fragment_slide_in_right,
        R.anim.fragment_slide_out_left,
        R.anim.fragment_slide_in_left,
        R.anim.fragment_slide_out_right
    )
}

fun FragmentTransaction.applySlideInLeftEnterSlideOutRightExitAnimation() {
    setCustomAnimations(
        R.anim.fragment_slide_in_left,
        R.anim.fragment_slide_out_right,
        R.anim.fragment_slide_in_right,
        R.anim.fragment_slide_out_left
    )
}
