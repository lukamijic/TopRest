package com.toprest.usercardlib.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.toprest.coreui.utils.BaseListAdapter
import com.toprest.coreui.utils.BaseViewHolder
import com.toprest.coreui.utils.onThrottledClick
import com.toprest.usercardlib.databinding.ItemUserBinding
import com.toprest.usercardlib.model.UserItem

class UserAdapter(
    private val layoutInflater: LayoutInflater,
    private val userClickedAction: (String) -> Unit
) : BaseListAdapter<UserItem, UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder = UserViewHolder(
        ItemUserBinding.inflate(
            layoutInflater,
            parent,
            false
        ),
        userClickedAction
    )

    class UserViewHolder(
        binding: ItemUserBinding,
        private val userClickedAction: (String) -> Unit
    ) : BaseViewHolder<UserItem, ItemUserBinding>(binding) {

        override fun ItemUserBinding.render(item: UserItem) {
            email.text = item.email
            root.onThrottledClick { userClickedAction(item.id) }
        }
    }
}
