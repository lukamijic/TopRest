package com.toprest.profile.ui

import android.os.Bundle
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.utils.onClick
import com.toprest.profile.databinding.FragmentProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<ProfileViewState, FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    companion object {

        const val TAG = "ProfileFragment"

        fun newInstance() = ProfileFragment()
    }

    override val model: ProfileViewModel by viewModel()

    override fun FragmentProfileBinding.initialiseView(savedInstanceState: Bundle?) {
        binding.logout.onClick { model.logout() }
        binding.addRest.onClick { model.openAddRestaurant() }
        binding.addRev.onClick { model.openLeaveReview() }
    }
}
