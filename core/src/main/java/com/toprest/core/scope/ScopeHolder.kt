package com.toprest.core.scope

import org.koin.core.scope.Scope

interface ScopeHolder {

    fun doesScopeExist(): Boolean

    fun createScope(owner: String? = null): Scope

    fun requireScope(): Scope

    fun closeScope(owner: String? = null)

    fun createScopeIfDoesNotExist(owner: String? = null)
}
