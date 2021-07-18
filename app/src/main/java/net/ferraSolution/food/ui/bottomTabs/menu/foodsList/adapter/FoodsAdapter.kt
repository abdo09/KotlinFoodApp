package net.ferraSolution.food.ui.bottomTabs.menu.foodsList.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.carousel_food.view.*
import net.ferraSolution.food.R
import net.ferraSolution.food.data.dao.CartItemDAO
import net.ferraSolution.food.data.models.Foods
import net.ferraSolution.food.utils.loadWithGlide

class FoodsAdapter : RecyclerView.Adapter<FoodsAdapter.QuoteViewHolder>() {

    inner class QuoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Foods>() {
        override fun areItemsTheSame(oldItem: Foods, newItem: Foods): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Foods, newItem: Foods): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        return QuoteViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.carousel_food,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onLikedClickListener: ((Foods) -> Unit)? = null
    private var onAddToCartClickListener: ((Foods) -> Unit)? = null
    private var onFoodClickListener: ((Foods) -> Unit)? = null

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val food = differ.currentList[position]
        holder.itemView.apply {

            tv_food_name.text = food.name

            food_img.context.apply {
                loadWithGlide(food_img, food.image)
            }

            food_card.setOnClickListener {
                onFoodClickListener?.let { it(food) }
            }

            btn_rate.apply {
                if (food.isLiked == true){
                    speed = 1f
                    playAnimation()
                }
                setOnClickListener {
                    if (food.isLiked == true){
                        speed = -1f
                        playAnimation()
                        food.isLiked = false
                    } else {
                        speed = 1f
                        playAnimation()
                        food.isLiked = true
                    }
                    onLikedClickListener?.let { it(food) }
                }
            }

            btn_cart.apply {
                setOnClickListener {
                    speed = 1f
                    playAnimation()
                    speed = -1f
                    playAnimation()
                    onAddToCartClickListener?.let { it(food) }
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (Foods) -> Unit) {
        onFoodClickListener = listener
    }

    fun setOnLikedClickListener(listener: (Foods) -> Unit) {
        onLikedClickListener = listener
    }

    fun setOnAddToCartClickListener(listener: (Foods) -> Unit) {
        onAddToCartClickListener = listener
    }

}