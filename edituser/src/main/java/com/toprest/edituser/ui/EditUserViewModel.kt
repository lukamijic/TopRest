package com.toprest.edituser.ui

import com.toprest.core.extension.shareReplayLatest
import com.toprest.coreui.BaseViewModel
import com.toprest.navigation.Router
import com.toprest.navigation.RoutingActionsDispatcher
import com.toprest.sessionlib.model.domain.User
import com.toprest.sessionlib.model.domain.UserType
import com.toprest.sessionlib.usecase.EditUser
import com.toprest.sessionlib.usecase.QueryUserById
import io.reactivex.rxjava3.core.Flowable.*
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.processors.BehaviorProcessor

class EditUserViewModel(
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher,
    queryUserById: QueryUserById,
    private val editUser: EditUser,
    private val userId: String
) : BaseViewModel<EditUserViewState>(
    mainThreadScheduler,
    backgroundScheduler,
    routingActionsDispatcher
) {

    private val user = queryUserById(userId).shareReplayLatest()

    private val firstName = BehaviorProcessor.create<String>()
    private val lastName = BehaviorProcessor.create<String>()
    private val userType = BehaviorProcessor.create<UserType>()

    private val isLoading = BehaviorProcessor.createDefault(false)

    init {
        query(user.map { EditUserViewState.InitialData(it.userType, it.firstName, it.lastName) }.take(1))
        query(isLoading.observeOn(backgroundScheduler).map(EditUserViewState::IsLoading))
        query(
            combineLatest(
                user,
                firstName,
                lastName,
                userType,
                ::isButtonEnabled
            ).map(EditUserViewState::EditButton)
        )
    }

    fun setFirstName(firstName: String) = this.firstName.onNext(firstName)

    fun setLastName(lastName: String) = this.lastName.onNext(lastName)

    fun setUserType(userType: UserType) = this.userType.onNext(userType)

    fun edit() {
        isLoading.onNext(true)
        runCommand(
            combineLatest(
                firstName,
                lastName,
                userType,
                { firstName, lastName, userType -> EditUser.Param(userId, firstName, lastName, userType) }
            ).firstOrError()
                .flatMapCompletable(editUser::invoke)
                .doOnComplete { close() }
                .doFinally { isLoading.onNext(false) }
        )
    }

    fun close() = dispatchRoutingAction(Router::closeEditUser)

    private fun isButtonEnabled(user: User, firstName: String, lastName: String, userType: UserType) =
        if (firstName.isBlank() || lastName.isBlank()) {
            false
        } else {
            user.firstName != firstName || user.lastName != lastName || user.userType != userType
        }
}
