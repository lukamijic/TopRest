package com.toprest.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.isVisible
import com.toprest.coreui.activity.BaseActivity
import com.toprest.coreui.utils.enable
import com.toprest.coreui.utils.fade
import com.toprest.coreui.utils.onClick
import com.toprest.coreui.utils.onThrottledClick
import com.toprest.ui.signup.databinding.ActivitySignUpBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : BaseActivity<SignUpViewState, ActivitySignUpBinding>(ActivitySignUpBinding::inflate) {

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, SignUpActivity::class.java)
    }

    override val model: SignUpViewModel by viewModel()

    override fun ActivitySignUpBinding.initialiseView(savedInstanceState: Bundle?) {
        back.onClick { model.previous() }
        next.onThrottledClick { model.actionButtonClicked() }
    }

    override fun render(viewState: SignUpViewState) = when(viewState) {
        is SignUpViewState.Title -> binding.renderTitle(viewState)
        is SignUpViewState.ActionButtonEnabled -> binding.renderActionButtonEnabled(viewState)
        is SignUpViewState.ActionButtonText -> binding.renderActionButtonText(viewState)
        is SignUpViewState.Loading -> binding.renderLoading(viewState)
    }

    override fun onBackPressed() {
        if (!binding.plateSpinner.isVisible) {
            model.previous()
        }
    }

    private fun ActivitySignUpBinding.renderTitle(viewState: SignUpViewState.Title) {
        title.text = viewState.title
    }

    private fun ActivitySignUpBinding.renderActionButtonEnabled(viewState: SignUpViewState.ActionButtonEnabled) {
        next.enable(viewState.isEnabled)
    }

    private fun ActivitySignUpBinding.renderActionButtonText(viewState: SignUpViewState.ActionButtonText) = with(next) {
        if ((currentView as TextView).text != viewState.text) {
            setText(viewState.text)
        }
    }

    private fun ActivitySignUpBinding.renderLoading(viewState: SignUpViewState.Loading) {
        plateSpinner.fade(viewState.isLoading)
    }
}
