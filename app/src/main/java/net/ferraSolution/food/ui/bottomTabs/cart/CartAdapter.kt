package net.ferraSolution.food.ui.bottomTabs.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.carousel_food.view.*
import kotlinx.android.synthetic.main.cart_item.view.*
import net.ferraSolution.food.R
import net.ferraSolution.food.data.models.OrderModel
import net.ferraSolution.food.utils.formatPrice
import net.ferraSolution.food.utils.loadWithGlide
import timber.log.Timber

class CartAdapter: RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<OrderModel.CartItem>() {
        override fun areItemsTheSame(oldItem: OrderModel.CartItem, newItem: OrderModel.CartItem): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: OrderModel.CartItem, newItem: OrderModel.CartItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cart_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((OrderModel.CartItem) -> Unit)? = null

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        val cartItem = differ.currentList[position]
        var totalPrice: Double

        holder.itemView.apply {

            cart_food_name.text = cartItem.foodName

            cart_food_image.context.apply {
                loadWithGlide(cart_food_image, cartItem.foodImage)
            }

            Timber.d("${cartItem.foodQuantity}")

            tv_current_value_cart.text = cartItem.foodQuantity.toString()

            totalPrice = (cartItem.foodPrice?.toDouble()?.let { cartItem.foodExtraPrice?.plus(it) } ?: 0.0).times(cartItem.foodQuantity)

            cart_food_price.text = totalPrice.formatPrice()


        }

    }

    fun setOnItemClickListener(listener: (OrderModel.CartItem) -> Unit) {
        onItemClickListener = listener
    }

}