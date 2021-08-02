package com.toprest.usercardlib.model

import com.toprest.coreui.utils.DiffUtilViewModel

data class UserItem(
    override val id: String,
    val email: String
) : DiffUtilViewModel(id)
