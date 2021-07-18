package net.ferraSolution.food.ui.bottomTabs.home.models

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.*
import net.ferraSolution.food.R
import net.ferraSolution.food.data.models.PopularCategoriesResponse
import net.ferraSolution.food.ui.bottomTabs.home.HomePageController

@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.carousel_popular_categories)
abstract class PopularCategoriesModel : EpoxyModelWithHolder<PopularCategoriesModel.Holder>(){

    @EpoxyAttribute
    var popularCategoriesList = listOf<PopularCategoriesResponse>()

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onViewAllClickedListener: View.OnClickListener

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var callBacks: HomePageController.AdapterCallbacks? = null

    override fun bind(holder: Holder) {
        super.bind(holder)

        holder.allCategoriesTitle.setOnClickListener(onViewAllClickedListener)

        //holder.myPopularCategoriesCarousel.numViewsToShowOnScreen = 3.5f
        holder.myPopularCategoriesCarousel.withModels {
            popularCategoriesList.forEachIndexed { index, popularCategoriesResponse ->
                PopularCategoriesItemModel_()
                    .id("$index$popularCategoriesResponse")
                    .category(popularCategoriesResponse)
                    .onCategoryClickedListener{ model, _, _, _ ->
                        callBacks?.onPopularCategoryClickLister(model.category())
                    }
                    .addTo(this)
            }
        }

    }

    class Holder : EpoxyHolder() {
        lateinit var myPopularCategoriesCarousel: Carousel
        lateinit var allCategoriesTitle: TextView
        override fun bindView(itemView: View) {
            myPopularCategoriesCarousel = itemView.findViewById(R.id.popular_categories_carousel)
            allCategoriesTitle = itemView.findViewById(R.id.all_categories_title)
        }
    }
}