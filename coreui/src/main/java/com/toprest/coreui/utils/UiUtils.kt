package com.toprest.coreui.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

open class DiffUtilCallback<T : DiffUtilViewModel> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
}

abstract class DiffUtilViewModel(open val id: Any? = null)
