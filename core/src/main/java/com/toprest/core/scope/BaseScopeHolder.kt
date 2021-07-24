package com.toprest.core.scope

import org.koin.core.context.GlobalContext
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

abstract class BaseScopeHolder(val name: String) : ScopeHolder {

    private var scope: Scope? = null

    var scopeOwner: String? = null
        private set

    override fun doesScopeExist(): Boolean = GlobalContext.get().getScopeOrNull(name) != null

    override fun requireScope(): Scope = scope!!

    override fun createScope(owner: String?): Scope {
        scope = GlobalContext.get().createScope(name, named(name))
        scopeOwner = owner
        return scope!!
    }

    override fun closeScope(owner: String?) {
        if (scopeOwner == owner) {
            scope?.close()
            scope = null
            scopeOwner = null
        }
    }

    override fun createScopeIfDoesNotExist(owner: String?) {
        if (!doesScopeExist()) {
            createScope(owner)
        }
    }
}
