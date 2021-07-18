package net.ferraSolution.food.ui.bottomTabs.home

import com.airbnb.epoxy.AsyncEpoxyController
import net.ferraSolution.food.data.models.BestDealModel
import net.ferraSolution.food.data.models.PopularCategoriesResponse
import net.ferraSolution.food.ui.bottomTabs.home.models.PopularCategoriesModel_
import net.ferraSolution.food.ui.bottomTabs.home.models.SliderCarouselModel_
import net.ferraSolution.food.ui.bottomTabs.home.models.ToolbarHomeModel_
import okhttp3.internal.immutableListOf

class HomePageController(private val callbacks: AdapterCallbacks) : AsyncEpoxyController() {

    var popularCategoriesList = immutableListOf<PopularCategoriesResponse>()
    var bestDealList = immutableListOf<BestDealModel>()

    override fun buildModels() {

        PopularCategoriesModel_()
            .id("PopularCategoriesModel_")
            .popularCategoriesList(popularCategoriesList)
            .onViewAllClickedListener { _, _, _, _ -> callbacks.onViewAllCategoriesClickLister() }
            .callBacks(callbacks)
            .addIf(popularCategoriesList.isNotEmpty(), this)

        SliderCarouselModel_()
            .id("SliderCarouselModel_")
            .imageList(bestDealList)
            .onViewAllPromotionListener { model, _, _, _ ->
                callbacks.onBestDealClickLister(model.image)
            }
            .addIf(bestDealList.isNotEmpty(), this)

    }

    interface AdapterCallbacks {
        fun onPopularCategoryClickLister(popularCategory : PopularCategoriesResponse)
        fun onBestDealClickLister(bestDealModel : BestDealModel)
        fun onViewAllCategoriesClickLister()
    }
}