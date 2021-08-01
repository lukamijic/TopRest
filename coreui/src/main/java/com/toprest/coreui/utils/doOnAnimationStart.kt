package com.toprest.coreui.utils

import android.animation.Animator
import android.view.ViewPropertyAnimator

inline fun ViewPropertyAnimator.doOnAnimationStart(
    crossinline action: (animator: Animator?) -> Unit
) = addAnimatorListener(onAnimationStart = action)

inline fun ViewPropertyAnimator.doOnAnimationEnd(
    crossinline action: (animator: Animator?) -> Unit
) = addAnimatorListener(onAnimationEnd = action)

inline fun ViewPropertyAnimator.addAnimatorListener(
    crossinline onAnimationCancel: (animation: Animator?) -> Unit = { _ -> },
    crossinline onAnimationStart: (animation: Animator?) -> Unit = { _ -> },
    crossinline onAnimationRepeat: (animation: Animator?) -> Unit = { _ -> },
    crossinline onAnimationEnd: (animation: Animator?) -> Unit = { _ -> }
): ViewPropertyAnimator {
    val animatorListener = object : Animator.AnimatorListener {
        override fun onAnimationCancel(animator: Animator?) {
            onAnimationCancel(animator)
        }

        override fun onAnimationEnd(animator: Animator?) {
            onAnimationEnd(animator)
        }

        override fun onAnimationRepeat(animator: Animator?) {
            onAnimationRepeat(animator)
        }

        override fun onAnimationStart(animator: Animator?) {
            onAnimationStart(animator)
        }
    }
    this.setListener(animatorListener)
    return this
}
