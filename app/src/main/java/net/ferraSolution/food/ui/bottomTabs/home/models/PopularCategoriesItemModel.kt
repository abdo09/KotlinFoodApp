package net.ferraSolution.food.ui.bottomTabs.home.models

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView
import de.hdodenhof.circleimageview.CircleImageView
import net.ferraSolution.food.R
import net.ferraSolution.food.data.models.PopularCategoriesResponse
import net.ferraSolution.food.utils.loadWithGlide

@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.carousel_categories_item)
abstract class PopularCategoriesItemModel  : EpoxyModelWithHolder<PopularCategoriesItemModel.Holder>() {

    @EpoxyAttribute
    lateinit var category: PopularCategoriesResponse

    @EpoxyAttribute(hash = false)
    lateinit var onCategoryClickedListener: View.OnClickListener

    override fun bind(holder: Holder) {
        super.bind(holder)

        category.let {
            holder.popularCategoryItem.setOnClickListener(onCategoryClickedListener)

            holder.popularCategoryImage.context.apply {
                loadWithGlide(holder.popularCategoryImage, it.image)
            }

            holder.popularCategoryName.text = it.name

        }

    }

    class Holder : EpoxyHolder() {
        lateinit var popularCategoryItem: MaterialCardView
        lateinit var popularCategoryName: TextView
        lateinit var popularCategoryImage: CircleImageView
        override fun bindView(itemView: View) {
            popularCategoryItem = itemView.findViewById(R.id.popular_category_item)
            popularCategoryName = itemView.findViewById(R.id.popular_category_name)
            popularCategoryImage = itemView.findViewById(R.id.popular_category_img)
        }
    }

}