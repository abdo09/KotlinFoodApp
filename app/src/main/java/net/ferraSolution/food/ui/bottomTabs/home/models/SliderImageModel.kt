/*
 * Copyright (c) 2018. this code belongs to Z3R0 any modifications is prohibited
 */

package net.ferraSolution.food.ui.bottomTabs.home.models

import android.view.View
import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView
import net.ferraSolution.food.R
import net.ferraSolution.food.data.models.BestDealModel
import net.ferraSolution.food.utils.loadWithGlide

@EpoxyModelClass(layout = R.layout.carousel_slider_image_model)
abstract class SliderImageModel : EpoxyModelWithHolder<SliderImageModel.Holder>() {
    @EpoxyAttribute
    lateinit var image: BestDealModel

    @EpoxyAttribute(hash = false)
    var onImageClickedItemListener: View.OnClickListener? = null

    override fun bind(holder: Holder) {
        image.let {

            holder.carItem.setOnClickListener(onImageClickedItemListener)

            holder.carBrandImage.context.apply {
                loadWithGlide(holder.carBrandImage, it.image)
            }

        }
    }

    override fun unbind(holder: Holder) {
        super.unbind(holder)
        holder.carItem.setOnClickListener(null)
    }

    class Holder : EpoxyHolder() {
        lateinit var carItem: MaterialCardView
        lateinit var carBrandImage: ImageView
        override fun bindView(itemView: View) {
            carItem = itemView.findViewById(R.id.car_item)
            carBrandImage = itemView.findViewById(R.id.car_brand_img)
        }
    }
}