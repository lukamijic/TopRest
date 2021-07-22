package com.toprest.coreui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.toprest.coreui.BaseViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber

abstract class BaseActivity<ViewState : Any, Binding : ViewBinding>(bindingInflater: (LayoutInflater) -> Binding) :
    BaseBindingActivity<Binding>(bindingInflater) {

    private var disposables = CompositeDisposable()

    abstract val model: BaseViewModel<ViewState>

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.viewStates().forEach { addDisposable(it.subscribe(this::render, Timber::e)) }
    }

    protected open fun render(viewState: ViewState) {
        // template
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    private fun addDisposable(disposable: Disposable) {
        require(!disposables.isDisposed)
        disposables.add(disposable)
    }
}
