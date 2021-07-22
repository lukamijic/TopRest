package com.toprest.landing.ui

import android.os.Bundle
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.utils.fadeIn
import com.toprest.coreui.utils.onClick
import com.toprest.landing.R
import com.toprest.landing.databinding.FragmentLandingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LandingFragment : BaseFragment<Unit, FragmentLandingBinding>(FragmentLandingBinding::inflate) {

    companion object {
        const val TAG = "LandingFragment"

        fun newInstance() = LandingFragment()
    }

    override val model: LandingViewModel by viewModel()

    override fun FragmentLandingBinding.initialiseView(savedInstanceState: Bundle?) {
        continueWithEmail.onClick { plateSpinner.fadeIn() }
        setupLogin()
    }

    private fun setupLogin() {
        binding.logIn.text = buildSpannedString {
            append(resources.getString(R.string.landing_already_have_an_account))
            append(" ")
            color(resources.getColor(R.color.landing_login_text_accent, null)) {
                append(resources.getString(R.string.landing_log_in))
            }
        }
    }
}
