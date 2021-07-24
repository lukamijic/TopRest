package com.toprest.ui.registerpassword

import android.os.Bundle
import com.coinme.formfieldlib.doAfterTextChanged
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.utils.fade
import com.toprest.coreui.utils.fadeIn
import com.toprest.coreui.utils.hideKeyboard
import com.toprest.ui.signup.databinding.FragmentRegisterPasswordBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterPasswordFragment : BaseFragment<RegisterPasswordViewState, FragmentRegisterPasswordBinding>(FragmentRegisterPasswordBinding::inflate) {

    companion object {
        const val TAG = "RegisterPasswordFragment"

        fun newInstance() = RegisterPasswordFragment()
    }

    override val model: RegisterPasswordViewModel by viewModel()

    override fun FragmentRegisterPasswordBinding.initialiseView(savedInstanceState: Bundle?) {
        root.hideKeyboard()

        password.doAfterTextChanged { model.setPassword(it.toString()) }
        repeatPassword.doAfterTextChanged { model.setRepeatedPassword(it.toString()) }
    }

    override fun render(viewState: RegisterPasswordViewState) = when(viewState) {
        is RegisterPasswordViewState.Password -> renderPassword(viewState)
        is RegisterPasswordViewState.PasswordError -> renderPasswordError(viewState)
        is RegisterPasswordViewState.RepeatedPassword -> renderRepeatedPassword(viewState)
        is RegisterPasswordViewState.RepeatedPasswordError -> renderRepeatedPasswordError(viewState)
        is RegisterPasswordViewState.AccountCreationError -> renderAccountCreationError(viewState)
    }

    private fun renderPassword(viewState: RegisterPasswordViewState.Password) {
        binding.password.setText(viewState.password)
    }

    private fun renderPasswordError(viewState: RegisterPasswordViewState.PasswordError) {
        binding.passwordErrorMessage.fade(viewState.isVisible)
    }

    private fun renderRepeatedPassword(viewState: RegisterPasswordViewState.RepeatedPassword) {
        binding.repeatPassword.setText(viewState.repeatedPassword)
    }

    private fun renderRepeatedPasswordError(viewState: RegisterPasswordViewState.RepeatedPasswordError) {
        binding.repeatPasswordErrorMessage.fade(viewState.isVisible)
    }

    private fun renderAccountCreationError(viewState: RegisterPasswordViewState.AccountCreationError) {
        binding.createAccountErrorMessage.fade(viewState.isVisible)
    }
}
