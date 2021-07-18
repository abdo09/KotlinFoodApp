package net.ferraSolution.food.data.repository.roomRepository

import androidx.lifecycle.LiveData
import net.ferraSolution.food.data.dao.CartItemDAO
import net.ferraSolution.food.data.models.OrderModel

interface CartRoomRepository {

    fun getAllCartFromDB(uid: String): List<OrderModel.CartItem>
    fun countItemInCart(uid: String): LiveData<Int?>
    fun sumPrice(uid: String): LiveData<Long>
    fun getItemInCart(foodID: String, uid: String): OrderModel.CartItem?
    fun getItemWithAllOptionsInCart(
        uid: String,
        foodId: String,
        foodSize: String,
        addon: String
    ): OrderModel.CartItem?
    fun upsertCartItem(cartItem: OrderModel.CartItem)
    fun deleteItemInCart(cartItem: OrderModel.CartItem)
    fun cleanCart(uid: String)

}

class CartRoomRepositoryImp(
    private val cartItemDAO: CartItemDAO
) : CartRoomRepository {
    override fun getAllCartFromDB(uid: String): List<OrderModel.CartItem> {
        return cartItemDAO.getAllCart(uid = uid)
    }

    override fun countItemInCart(uid: String): LiveData<Int?> {
        return cartItemDAO.countItemInCart(uid = uid)
    }

    override fun sumPrice(uid: String): LiveData<Long> {
        return cartItemDAO.sumPrice(uid = uid)
    }

    override fun getItemInCart(foodID: String, uid: String): OrderModel.CartItem? {
        return cartItemDAO.getItemInCart(foodId = foodID, uid = uid)
    }

    override fun getItemWithAllOptionsInCart(
        uid: String,
        foodId: String,
        foodSize: String,
        addon: String
    ): OrderModel.CartItem? {
        return cartItemDAO.getItemWithAllOptionsInCart(
            uid = uid,
            foodId = foodId,
            foodSize = foodSize,
            addon = addon
        )
    }

    override fun upsertCartItem(cartItem: OrderModel.CartItem) {
        cartItemDAO.upsertItemInCart(cartItem = cartItem)
    }

    override fun deleteItemInCart(cartItem: OrderModel.CartItem) {
        return cartItemDAO.deleteItemInCart(cartItem = cartItem)
    }

    override fun cleanCart(uid: String) {
        return cartItemDAO.cleanCart(uid = uid)
    }

}