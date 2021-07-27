package com.toprest.addrestaurant.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import com.coinme.formfieldlib.doAfterTextChanged
import com.toprest.addrestaurant.databinding.FragmentAddRestaurantBinding
import com.toprest.coreui.BaseFragment
import com.toprest.coreui.lifecycleobservers.WindowInputModeLifecycleObserver
import com.toprest.coreui.utils.enable
import com.toprest.coreui.utils.fade
import com.toprest.coreui.utils.onClick
import com.toprest.coreui.utils.onThrottledClick
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val EMPTY_SPACE_MAX_ALPHA = 0.6f

class AddRestaurantFragment : BaseFragment<AddRestaurantViewState, FragmentAddRestaurantBinding>(FragmentAddRestaurantBinding::inflate) {

    companion object {
        const val TAG = "AddRestaurantFragment"

        fun newInstance() = AddRestaurantFragment()
    }

    override val model: AddRestaurantViewModel by viewModel()

    private val windowInputModeLifecycleObserver by lazy { WindowInputModeLifecycleObserver(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN) { requireActivity().window } }

    override fun FragmentAddRestaurantBinding.initialiseView(savedInstanceState: Bundle?) {
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
        complete.onThrottledClick { model.addRestaurant() }
    }

    override fun render(viewState: AddRestaurantViewState) = when(viewState) {
        is AddRestaurantViewState.Button -> binding.complete.enable(viewState.isEnabled)
        is AddRestaurantViewState.Loading -> binding.plateSpinner.fade(viewState.isVisible)
    }

    override fun back(): Boolean = binding.actionSheet.close()
}
