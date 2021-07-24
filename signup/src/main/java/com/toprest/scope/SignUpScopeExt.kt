package com.toprest.scope

import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

inline fun <reified T> Scope.signUpGet(qualifier: Qualifier? = null): T = getScope(SIGN_UP_SCOPE).get(qualifier)
inline fun <reified T> Scope.signUpGet(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T = getScope(SIGN_UP_SCOPE).get(qualifier, parameters)
