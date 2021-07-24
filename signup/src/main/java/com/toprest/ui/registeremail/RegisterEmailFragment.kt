package com.toprest.ui.registeremail

import android.os.Bundle
import com.coinme.formfieldlib.doAfterTextChanged
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.utils.fade
import com.toprest.coreui.utils.hideKeyboard
import com.toprest.ui.signup.databinding.FragmentRegisterEmailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterEmailFragment : BaseFragment<RegisterEmailViewState, FragmentRegisterEmailBinding>(FragmentRegisterEmailBinding::inflate) {

    companion object {
        const val TAG = "RegisterEmailFragment"

        fun newInstance() = RegisterEmailFragment()
    }

    override val model: RegisterEmailViewModel by viewModel()

    override fun FragmentRegisterEmailBinding.initialiseView(savedInstanceState: Bundle?) {
        root.hideKeyboard()
        email.doAfterTextChanged { model.setEmail(it.toString()) }
    }

    override fun render(viewState: RegisterEmailViewState) = when(viewState) {
        is RegisterEmailViewState.Email -> renderEmail(viewState)
        is RegisterEmailViewState.Error -> renderError(viewState)
    }

    private fun renderEmail(viewState: RegisterEmailViewState.Email) {
        binding.email.setText(viewState.email)
    }

    private fun renderError(viewState: RegisterEmailViewState.Error) {
        binding.emailErrorMessage.fade(viewState.showError)
    }
}
