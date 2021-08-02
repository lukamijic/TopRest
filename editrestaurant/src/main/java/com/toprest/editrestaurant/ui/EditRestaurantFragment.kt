package com.toprest.editrestaurant.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.core.os.bundleOf
import com.coinme.formfieldlib.doAfterTextChanged
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.lifecycleobservers.WindowInputModeLifecycleObserver
import com.toprest.coreui.utils.enable
import com.toprest.coreui.utils.fade
import com.toprest.coreui.utils.onClick
import com.toprest.coreui.utils.onThrottledClick
import com.toprest.editrestaurant.databinding.FragmentEditRestaurantBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val EMPTY_SPACE_MAX_ALPHA = 0.6f

class EditRestaurantFragment : BaseFragment<EditRestaurantViewState, FragmentEditRestaurantBinding>(FragmentEditRestaurantBinding::inflate) {

    companion object {
        const val TAG = "EditRestaurantFragment"

        private const val RESTAURANT_ID_KEY = "RESTAURANT_ID_KEY"

        fun newInstance(restaurantId: String) = EditRestaurantFragment().apply {
            arguments = bundleOf(RESTAURANT_ID_KEY to restaurantId)
        }
    }

    override val model: EditRestaurantViewModel by viewModel(parameters = { parametersOf(restaurantId) })

    private val restaurantId by lazy { requireArguments().getString(RESTAURANT_ID_KEY) }

    private val windowInputModeLifecycleObserver by lazy { WindowInputModeLifecycleObserver(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN) { requireActivity().window } }

    override fun FragmentEditRestaurantBinding.initialiseView(savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycle.addObserver(windowInputModeLifecycleObserver)
        actionSheet.onCollapsedListener = {
            model.close()
        }

        root.onClick { back() }

        actionSheet.onSlideListener = { _, slideOffset ->
            emptySpace.alpha = EMPTY_SPACE_MAX_ALPHA * slideOffset
        }

        restaurantName.doAfterTextChanged { model.setRestaurantTitle(it.toString()) }
        restaurantDescription.doAfterTextChanged { model.setRestaurantDescription(it.toString()) }
        complete.onThrottledClick { model.edit() }
        delete.onThrottledClick { model.delete() }
    }

    override fun render(viewState: EditRestaurantViewState) = when(viewState) {
        is EditRestaurantViewState.Button -> binding.complete.enable(viewState.isEnabled)
        is EditRestaurantViewState.Loading -> binding.plateSpinner.fade(viewState.isVisible)
        is EditRestaurantViewState.InitialData -> {
            binding.restaurantName.setText(viewState.restaurantName)
            binding.restaurantDescription.setText(viewState.restaurantDescription)
        }
    }

    override fun back(): Boolean = binding.actionSheet.close()
}
