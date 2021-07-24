package com.toprest.ui.accounttype

import com.toprest.controller.SignUpController
import com.toprest.coreui.BaseViewModel
import com.toprest.model.AccountType
import com.toprest.model.SignUpScreen
import com.toprest.navigation.RoutingActionsDispatcher
import io.reactivex.rxjava3.core.Scheduler

class AccountTypeSelectionViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    private val controller: SignUpController
) : BaseViewModel<AccountTypeSelectionViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    init {
        runCommand(controller.setScreen(SignUpScreen.SELECT_ACCOUNT_TYPE))

        query(controller.accountType().map(::AccountTypeSelectionViewState).take(1))
    }

    fun setAccountType(accountType: AccountType) = runCommand(controller.setAccountType(accountType))
}
