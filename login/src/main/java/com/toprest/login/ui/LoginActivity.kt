package com.toprest.login.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import com.coinme.formfieldlib.doAfterTextChanged
import com.toprest.coreui.activity.BaseActivity
import com.toprest.coreui.utils.enable
import com.toprest.coreui.utils.fade
import com.toprest.coreui.utils.onClick
import com.toprest.login.databinding.ActivityLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<LoginViewState, ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }

    override val model: LoginViewModel by viewModel()

    override fun ActivityLoginBinding.initialiseView(savedInstanceState: Bundle?) {
        binding.login.enable(false)
        binding.login.onClick { model.login() }

        binding.email.doAfterTextChanged { model.setEmail(it.toString()) }
        binding.password.doAfterTextChanged { model.setPassword(it.toString()) }
    }

    override fun render(viewState: LoginViewState) = when(viewState) {
        is LoginViewState.LoginButton -> binding.login.enable(viewState.enabled)
        is LoginViewState.EmailError -> binding.emailErrorMessage.fade(viewState.isVisible)
        is LoginViewState.PasswordError -> binding.passwordErrorMessage.fade(viewState.isVisible)
        is LoginViewState.LoginError -> binding.loginErrorMessage.fade(viewState.isVisible)
        is LoginViewState.Loading -> binding.plateSpinner.fade(viewState.isLoading)
    }

    override fun onBackPressed() {
        if (!binding.plateSpinner.isVisible) {
            super.onBackPressed()
        }
    }
}
