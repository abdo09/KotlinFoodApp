package net.ferraSolution.food.ui.bottomTabs.home.models

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import net.ferraSolution.food.R

@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.toolbar_with_logo)
abstract class ToolbarHomeModel : EpoxyModelWithHolder<ToolbarHomeModel.Holder>() {

    @EpoxyAttribute
    var title: String = ""

    override fun bind(holder: Holder) {
        holder.title.text = title
    }

    class Holder : EpoxyHolder() {
        lateinit var title: TextView

        override fun bindView(itemView: View) {
            title = itemView.findViewById(R.id.fragment_title)
        }
    }


}