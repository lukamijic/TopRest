package com.toprest.ui.accounttype

import android.os.Bundle
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.utils.hideKeyboard
import com.toprest.model.AccountType
import com.toprest.ui.signup.databinding.FragmentAccountTypeSelectionBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountTypeSelectionFragment : BaseFragment<AccountTypeSelectionViewState, FragmentAccountTypeSelectionBinding>(FragmentAccountTypeSelectionBinding::inflate) {

    companion object {
        const val TAG = "AccountTypeFragment"

        fun newInstance() = AccountTypeSelectionFragment()
    }

    override val model : AccountTypeSelectionViewModel by viewModel()

    override fun FragmentAccountTypeSelectionBinding.initialiseView(savedInstanceState: Bundle?) {
        root.hideKeyboard()
        radioGroup.setOnCheckedChangeListener { _, _ ->
            when {
                customerRadioButton.isChecked -> model.setAccountType(AccountType.CUSTOMER)
                ownerRadioButton.isChecked -> model.setAccountType(AccountType.OWNER)
            }
        }
    }

    override fun render(viewState: AccountTypeSelectionViewState) {
        when (viewState.accountType) {
            AccountType.CUSTOMER -> binding.customerRadioButton.isChecked = true
            AccountType.OWNER -> binding.ownerRadioButton.isChecked = true
        }
    }
}
