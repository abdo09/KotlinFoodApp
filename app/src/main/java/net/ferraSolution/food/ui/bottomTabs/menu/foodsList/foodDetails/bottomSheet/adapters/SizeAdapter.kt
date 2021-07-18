package net.ferraSolution.food.ui.bottomTabs.menu.foodsList.foodDetails.bottomSheet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.carousel_addon_size_item.view.*
import net.ferraSolution.food.R
import net.ferraSolution.food.data.models.SizeModel

class SizeAdapter : RecyclerView.Adapter<SizeAdapter.SizeViewHolder>() {

    inner class SizeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<SizeModel>() {
        override fun areItemsTheSame(oldItem: SizeModel, newItem: SizeModel): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: SizeModel, newItem: SizeModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        return SizeViewHolder(
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

    private var onSizeModelClickListener: ((SizeModel) -> Unit)? = null
    private var sizePosition: ((Int) -> Unit)? = null

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        val size = differ.currentList[position]

        holder.itemView.apply {
            this.tv_addon_size_name.text = size.name
            this.tv_addon_size_price.text = size.price.toString()

            this.setOnClickListener {

                addon_size_radio_button.apply {
                    if (size.taken == true){
                        speed = 2f
                        playAnimation()
                    }
                    if (size.taken == true){
                        speed = -2f
                        playAnimation()
                        size.taken = false
                    } else {
                        speed = 2f
                        playAnimation()
                        size.taken = true
                    }
                    onSizeModelClickListener?.let { it(size) }
                    sizePosition?.let { it(position) }
                }

            }
        }
    }

    fun setOnItemClickListener(listener: (SizeModel) -> Unit) {
        onSizeModelClickListener = listener
    }

    fun setPositionClickListener(listener: (Int) -> Unit) {
        sizePosition = listener
    }

}