package com.toprest.ui.usertype

import android.os.Bundle
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.utils.hideKeyboard
import com.toprest.sessionlib.model.domain.UserType
import com.toprest.ui.signup.databinding.FragmentUserTypeSelectionBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserTypeSelectionFragment : BaseFragment<UserTypeSelectionViewState, FragmentUserTypeSelectionBinding>(FragmentUserTypeSelectionBinding::inflate) {

    companion object {
        const val TAG = "UserTypeSelectionFragment"

        fun newInstance() = UserTypeSelectionFragment()
    }

    override val model : UserTypeSelectionViewModel by viewModel()

    override fun FragmentUserTypeSelectionBinding.initialiseView(savedInstanceState: Bundle?) {
        root.hideKeyboard()
        radioGroup.setOnCheckedChangeListener { _, _ ->
            when {
                customerRadioButton.isChecked -> model.setUserType(UserType.CUSTOMER)
                ownerRadioButton.isChecked -> model.setUserType(UserType.OWNER)
            }
        }
    }

    override fun render(viewState: UserTypeSelectionViewState) {
        when (viewState.userType) {
            UserType.CUSTOMER -> binding.customerRadioButton.isChecked = true
            UserType.OWNER -> binding.ownerRadioButton.isChecked = true
            else -> { }
        }
    }
}
