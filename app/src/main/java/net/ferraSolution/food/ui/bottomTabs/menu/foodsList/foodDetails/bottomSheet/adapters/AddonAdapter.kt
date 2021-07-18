package net.ferraSolution.food.ui.bottomTabs.menu.foodsList.foodDetails.bottomSheet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.carousel_addon_size_item.view.*
import net.ferraSolution.food.R
import net.ferraSolution.food.data.models.AddonModel

class AddonAdapter : RecyclerView.Adapter<AddonAdapter.AddonViewHolder>() {

    inner class AddonViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<AddonModel>() {
        override fun areItemsTheSame(oldItem: AddonModel, newItem: AddonModel): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: AddonModel, newItem: AddonModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddonAdapter.AddonViewHolder {
        return AddonViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.carousel_addon_size_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onAddonModelClickListener: ((AddonModel) -> Unit)? = null

    override fun onBindViewHolder(holder: AddonAdapter.AddonViewHolder, position: Int) {
        val addon = differ.currentList[position]

        holder.itemView.apply {
            this.tv_addon_size_name.text = addon.name
            this.tv_addon_size_price.text = addon.price.toString()

            addon_size_radio_button.apply {
                if (addon.taken == true) {
                    speed = 2f
                    playAnimation()
                }
            }

            this.setOnClickListener {

                addon_size_radio_button.apply {
                    if (addon.taken == true){
                        speed = -2f
                        playAnimation()
                        addon.taken = false
                    } else {
                        speed = 2f
                        playAnimation()
                        addon.taken = true
                    }
                    onAddonModelClickListener?.let { it(addon) }
                }

            }
        }
    }

    fun setOnItemClickListener(listener: (AddonModel) -> Unit) {
        onAddonModelClickListener = listener
    }

}