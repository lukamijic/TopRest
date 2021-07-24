package com.toprest.ui.registername

import android.os.Bundle
import com.coinme.formfieldlib.doAfterTextChanged
import com.toprest.coreui.BaseFragment
import com.toprest.ui.signup.databinding.FragmentRegisterNameBinding
import org.koin.android.ext.android.inject

class RegisterNameFragment : BaseFragment<RegisterNameViewState, FragmentRegisterNameBinding>(FragmentRegisterNameBinding::inflate) {

    companion object {
        const val TAG = "RegisterNameFragment"

        fun newInstance() = RegisterNameFragment()
    }

    override val model: RegisterNameViewModel by inject()

    override fun FragmentRegisterNameBinding.initialiseView(savedInstanceState: Bundle?) {
        firstName.doAfterTextChanged { model.setFirstName(it.toString()) }
        lastName.doAfterTextChanged { model.setLastName(it.toString()) }
    }

    override fun render(viewState: RegisterNameViewState) {
        binding.firstName.setText(viewState.firstName)
        binding.lastName.setText(viewState.lastName)
    }
}
