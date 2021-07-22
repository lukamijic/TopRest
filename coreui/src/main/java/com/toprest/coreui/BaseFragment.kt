package com.toprest.coreui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.toprest.navigation.BackPropagatingFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber

abstract class BaseFragment<ViewState : Any, Binding : ViewBinding>(private val bindingInflater: (LayoutInflater) -> Binding) : Fragment(), BackPropagatingFragment {

    private var disposables = CompositeDisposable()

    private var _binding: Binding? = null

    protected val binding: Binding
        get() = _binding!!

    abstract val model: BaseViewModel<ViewState>

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = bindingInflater(layoutInflater)
        return binding.root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isClickable = true // To avoid clicks passing through
        binding.initialiseView(savedInstanceState)
        model.viewStates().forEach { addDisposable(it.subscribe(this::render, Timber::e)) }
    }

    /** Override to initialise view */
    open fun Binding.initialiseView(savedInstanceState: Bundle?) {
        // Template
    }

    protected open fun render(viewState: ViewState) {
        // template
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

    override fun back() = false

    private fun addDisposable(disposable: Disposable) {
        require(!disposables.isDisposed)
        disposables.add(disposable)
    }
}
