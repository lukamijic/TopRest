package com.top.restaurantcardlib.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.top.restaurantcardlib.databinding.ItemRestaurantCardBinding
import com.top.restaurantcardlib.model.RestaurantOverview
import com.toprest.coreui.utils.BaseListAdapter
import com.toprest.coreui.utils.BaseViewHolder

class RestaurantAdapter(
    private val layoutInflater: LayoutInflater,
    private val clickAction: (String) -> Unit
) : BaseListAdapter<RestaurantOverview, RestaurantAdapter.RestaurantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder = RestaurantViewHolder(
        ItemRestaurantCardBinding.inflate(
            layoutInflater,
            parent,
            false
        ),
        clickAction
    )

    class RestaurantViewHolder(
        binding: ItemRestaurantCardBinding,
        private val clickAction: (String) -> Unit
    ) : BaseViewHolder<RestaurantOverview, ItemRestaurantCardBinding>(binding) {

        override fun ItemRestaurantCardBinding.render(item: RestaurantOverview) {
            root.setOnClickListener { clickAction(item.id) }
            name.text = item.name
            description.text = item.description
            score.text = item.averageScore?.let { String.format("%.1f/5", item.averageScore) } ?: "N/A"
        }
    }
}
