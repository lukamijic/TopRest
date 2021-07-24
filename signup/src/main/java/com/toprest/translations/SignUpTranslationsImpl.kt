package com.toprest.translations

import android.content.res.Resources
import com.toprest.ui.signup.R

class SignUpTranslationsImpl(private val resources: Resources) : SignUpTranslations {

    override fun nameTitle(): String = resources.getString(R.string.registername_title)

    override fun accountTypeTitle(): String = resources.getString(R.string.accounttype_title)

    override fun emailTitle(): String = resources.getString(R.string.registeremail_title)

    override fun passwordTitle(): String = resources.getString(R.string.registerpassword_title)

    override fun next(): String = resources.getString(R.string.signup_action_button_next)

    override fun createAccount(): String = resources.getString(R.string.signup_action_button_create_account)
}
