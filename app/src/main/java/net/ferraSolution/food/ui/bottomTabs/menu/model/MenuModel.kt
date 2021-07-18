package net.ferraSolution.food.ui.bottomTabs.menu.model

import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import net.ferraSolution.food.BR
import net.ferraSolution.food.R
import net.ferraSolution.food.data.models.CategoryModel


@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.carousel_menu)
abstract class MenuModel: DataBindingEpoxyModel() {

    @EpoxyAttribute(hash = false)
    lateinit var category: CategoryModel

    @EpoxyAttribute(hash = false)
    var clickListener: View.OnClickListener? = null

    override fun setDataBindingVariables(binding: ViewDataBinding?) {
        binding?.setVariable(BR.category_title, category.name)
        binding?.setVariable(BR.on_categoryClickedLister, clickListener)

        binding?.root?.let {
            Glide.with(it)
                .load(category.image)
                .apply(RequestOptions.circleCropTransform().centerCrop())
                .into(it.findViewById(R.id.menu_img))
        }
    }

}