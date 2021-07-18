package net.ferraSolution.food.ui.bottomTabs.menu

import com.airbnb.epoxy.AsyncEpoxyController
import net.ferraSolution.food.data.models.CategoryModel
import net.ferraSolution.food.ui.bottomTabs.menu.model.MenuModel_
import okhttp3.internal.immutableListOf

class MenuFragmentController(private val callbacks: AdapterCallbacks) : AsyncEpoxyController()  {

    var categoryList = immutableListOf<CategoryModel?>()

    override fun buildModels() {

        categoryList.forEachIndexed { index, categoryModel ->
            MenuModel_()
                .id("MenuModel_$index")
                .category(categoryModel)
                .clickListener{ model, _ , _ , _ ->
                    callbacks.onCategoryClickedLister(model.category)
                }
                .addIf(categoryList.isNotEmpty(), this)
        }

    }

    interface AdapterCallbacks {
        fun onCategoryClickedLister(categoryModel: CategoryModel)
    }

}