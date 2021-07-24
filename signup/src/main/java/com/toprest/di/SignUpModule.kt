package com.toprest.di

import com.toprest.controller.SignUpController
import com.toprest.controller.SignUpControllerImpl
import com.toprest.core.di.BACKGROUND_SCHEDULER
import com.toprest.core.di.MAIN_SCHEDULER
import com.toprest.core.di.SERIAL_BACKGROUND_SCHEDULER
import com.toprest.scope.SIGN_UP_SCOPE
import com.toprest.scope.SignUpScopeHolder
import com.toprest.scope.signUpGet
import com.toprest.sessionlib.usecase.CreateUser
import com.toprest.translations.SignUpTranslations
import com.toprest.translations.SignUpTranslationsImpl
import com.toprest.ui.usertype.UserTypeSelectionViewModel
import com.toprest.ui.registeremail.RegisterEmailViewModel
import com.toprest.ui.registername.RegisterNameViewModel
import com.toprest.ui.registerpassword.RegisterPasswordViewModel
import com.toprest.ui.signup.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun signUpModule() : Module = module {

    viewModel {
        SignUpViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            signUpScopeHolder = get<SignUpScopeHolder>().apply { createScope() },
            controller = signUpGet()
        )
    }

    viewModel {
        RegisterNameViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            controller = signUpGet()
        )
    }

    viewModel {
        UserTypeSelectionViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            controller = signUpGet()
        )
    }

    viewModel {
        RegisterEmailViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            controller = signUpGet()
        )
    }

    viewModel {
        RegisterPasswordViewModel(
            mainThreadScheduler = get(named(MAIN_SCHEDULER)),
            backgroundScheduler = get(named(BACKGROUND_SCHEDULER)),
            routingActionsDispatcher = get(),
            controller = signUpGet()
        )
    }

    single { SignUpScopeHolder() }

    single<SignUpTranslations> { SignUpTranslationsImpl(androidContext().resources) }

    scope(named(SIGN_UP_SCOPE)) {

        scoped<SignUpController> {
            SignUpControllerImpl(
                translations = get(),
                serialScheduler = get(named(SERIAL_BACKGROUND_SCHEDULER)),
                routingActionsDispatcher = get(),
                createUser = get<CreateUser>()
            )
        }
    }
}
