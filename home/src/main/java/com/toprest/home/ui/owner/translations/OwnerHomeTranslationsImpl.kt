package com.toprest.home.ui.owner.translations

import android.content.res.Resources
import com.toprest.home.R

class OwnerHomeTranslationsImpl(private val resources: Resources) : OwnerHomeTranslations {

    override fun restaurantsTitles(): String = resources.getString(R.string.ownerhome_restaurant_title)

    override fun reviewsTitle(): String = resources.getString(R.string.ownerhome_reviews_title)
}
