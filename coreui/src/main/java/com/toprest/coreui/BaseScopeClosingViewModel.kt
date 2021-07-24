package com.toprest.coreui

import com.toprest.core.scope.ScopeHolder
import com.toprest.navigation.RoutingActionsDispatcher
import io.reactivex.rxjava3.core.Scheduler

abstract class BaseScopeClosingViewModel<BaseViewState : Any>(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    private val scopeHolder: ScopeHolder
) : BaseViewModel<BaseViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    open val scopeOwner: String? = null

    override fun onCleared() {
        scopeHolder.closeScope(scopeOwner)
        super.onCleared()
    }
}
