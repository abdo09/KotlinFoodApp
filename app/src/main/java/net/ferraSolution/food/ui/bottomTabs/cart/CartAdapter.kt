package net.ferraSolution.food.ui.bottomTabs.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.carousel_food.view.*
import kotlinx.android.synthetic.main.cart_item.view.*
import kotlinx.android.synthetic.main.fragment_food_details.*
import kotlinx.android.synthetic.main.fragment_food_details.view.*
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

    private var onItemClickListener: ((List<OrderModel.CartItem>) -> Unit)? = null

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        val cartItem = differ.currentList[position]

        holder.itemView.apply {

            cart_food_name.text = cartItem.foodName

            cart_food_image.context.apply {
                loadWithGlide(cart_food_image, cartItem.foodImage)
            }

            Timber.d("${cartItem.foodQuantity}")

            tv_current_value_cart.text = cartItem.foodQuantity.toString()

            calculateItemTotalPrice(tv_current_value_cart, cart_food_price, cartItem)

            btn_plus_cart.setOnClickListener {
                addOne(tv_current_value_cart)
                calculateItemTotalPrice(tv_current_value_cart, cart_food_price, cartItem)
                cartItem.foodQuantity = tv_current_value_cart.text.toString().toInt()
                onItemClickListener?.let { it(differ.currentList) }
            }

            btn_minus_cart.setOnClickListener {
                subOne(tv_current_value_cart)
                calculateItemTotalPrice(tv_current_value_cart, cart_food_price, cartItem)
                cartItem.foodQuantity = tv_current_value_cart.text.toString().toInt()
                onItemClickListener?.let { it(differ.currentList) }
            }

        }

    }

    fun setOnItemClickListener(listener: (List<OrderModel.CartItem>) -> Unit) {
        onItemClickListener = listener
    }

    private fun addOne(tv_current_value: TextView) {
        var quantity = tv_current_value.text.toString().toInt()
        quantity++
        tv_current_value.text = quantity.toString()
    }

    private fun subOne(tv_current_value: TextView) {
        var quantity = tv_current_value.text.toString().toInt()
        if (quantity >= 2)
            quantity--
        tv_current_value.text = quantity.toString()
    }

    private fun calculateItemTotalPrice(tv_current_value_cart: TextView, cart_food_price: TextView, cartItem: OrderModel.CartItem) {
        val totalPrice: Double = (cartItem.foodPrice?.toDouble()?.let { cartItem.foodExtraPrice?.plus(it) } ?: 0.0).times(tv_current_value_cart.text.toString().toInt())

        cart_food_price.text = totalPrice.formatPrice()
    }

}