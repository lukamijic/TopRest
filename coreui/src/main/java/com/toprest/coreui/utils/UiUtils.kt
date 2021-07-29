package com.toprest.coreui.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class DiffUtilCallback<T : DiffUtilViewModel> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
}

abstract class DiffUtilViewModel(open val id: Any? = null)

abstract class BaseListAdapter<Item : DiffUtilViewModel, ViewHolder : BaseViewHolder<Item, out ViewBinding>> :
    ListAdapter<Item, ViewHolder>(DiffUtilCallback()) {

    override fun onViewRecycled(holder: ViewHolder) {
        holder.clear()
        super.onViewRecycled(holder)
    }

    open override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.render(getItem(position))
}

abstract class BaseViewHolder<T : DiffUtilViewModel, Binding : ViewBinding>(
    protected val binding: Binding
) : RecyclerView.ViewHolder(binding.root) {

    fun render(item: T) = binding.render(item)

    abstract fun Binding.render(item: T)

    open fun clear() {
        // template
    }
}
