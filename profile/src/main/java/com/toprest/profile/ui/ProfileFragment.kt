package com.toprest.profile.ui

import android.os.Bundle
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.utils.onClick
import com.toprest.coreui.utils.onThrottledClick
import com.toprest.profile.databinding.FragmentProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<ProfileViewState, FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    companion object {

        const val TAG = "ProfileFragment"

        fun newInstance() = ProfileFragment()
    }

    override val model: ProfileViewModel by viewModel()

    override fun FragmentProfileBinding.initialiseView(savedInstanceState: Bundle?) {
        logout.onThrottledClick { model.logout() }
    }

    override fun render(viewState: ProfileViewState) = with(binding) {
        firstName.text = viewState.firstName
        lastName.text = viewState.lastName
        email.text = viewState.email
        userType.text = viewState.userType
    }
}
