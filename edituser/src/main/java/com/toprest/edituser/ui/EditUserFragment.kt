package com.toprest.edituser.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.core.os.bundleOf
import com.coinme.formfieldlib.doAfterTextChanged
import com.google.android.material.tabs.TabLayout
import com.toprest.commonui.utils.TabSelectedAdapter
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.lifecycleobservers.WindowInputModeLifecycleObserver
import com.toprest.coreui.utils.enable
import com.toprest.coreui.utils.fade
import com.toprest.coreui.utils.onClick
import com.toprest.coreui.utils.onThrottledClick
import com.toprest.edituser.databinding.FragmentEditUserBinding
import com.toprest.sessionlib.model.domain.UserType
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val EMPTY_SPACE_MAX_ALPHA = 0.6f

class EditUserFragment : BaseFragment<EditUserViewState, FragmentEditUserBinding>(FragmentEditUserBinding::inflate) {

    companion object {
        const val TAG = "EditUserFragment"
        private const val USER_ID_KEY = "USER_ID_KEY"

        fun newInstance(userId: String) = EditUserFragment().apply {
            arguments = bundleOf(USER_ID_KEY to userId)
        }
    }

    override val model: EditUserViewModel by viewModel(parameters = { parametersOf(userId) })

    private val userId by lazy { requireArguments().getString(USER_ID_KEY) }

    private val windowInputModeLifecycleObserver by lazy { WindowInputModeLifecycleObserver(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN) { requireActivity().window } }

    override fun FragmentEditUserBinding.initialiseView(savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycle.addObserver(windowInputModeLifecycleObserver)
        actionSheet.onCollapsedListener = {
            model.close()
        }

        root.onClick { back() }

        actionSheet.onSlideListener = { _, slideOffset ->
            emptySpace.alpha = EMPTY_SPACE_MAX_ALPHA * slideOffset
        }

        tabs.apply {
            clipToOutline = true
            addOnTabSelectedListener(TabSelectedAdapter(tabSelected = ::handleTabSelected))
        }


        firstName.doAfterTextChanged { model.setFirstName(it.toString()) }
        lastName.doAfterTextChanged { model.setLastName(it.toString()) }
        edit.onThrottledClick { model.edit() }
    }

    override fun render(viewState: EditUserViewState) = when(viewState) {
        is EditUserViewState.InitialData -> binding.renderInitialData(viewState)
        is EditUserViewState.IsLoading -> binding.renderLoading(viewState)
        is EditUserViewState.EditButton -> binding.renderEditButton(viewState)
    }

    private fun FragmentEditUserBinding.renderInitialData(viewState: EditUserViewState.InitialData) {
        firstName.setText(viewState.firstName)
        lastName.setText(viewState.lastName)
        tabs.getTabAt(viewState.userType.ordinal)?.select()
        model.setUserType(viewState.userType)
    }

    private fun FragmentEditUserBinding.renderLoading(viewState: EditUserViewState.IsLoading) {
        plateSpinner.fade(viewState.isLoading)
    }

    private fun FragmentEditUserBinding.renderEditButton(viewState: EditUserViewState.EditButton) {
        edit.enable(viewState.isEnabled)
    }

    private fun handleTabSelected(tab: TabLayout.Tab?) {
        tab?.position?.let {
            model.setUserType(
                when(it) {
                    0 -> UserType.CUSTOMER
                    1 -> UserType.OWNER
                    2 -> UserType.ADMIN
                    else -> throw IllegalArgumentException("Invalid tab")
                }
            )
        }
    }

    override fun back(): Boolean = binding.actionSheet.close()
}
