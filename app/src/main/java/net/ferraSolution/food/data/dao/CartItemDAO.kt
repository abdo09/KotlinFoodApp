package net.ferraSolution.food.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import net.ferraSolution.food.data.models.OrderModel

@Dao
abstract class CartItemDAO : BaseDao<OrderModel.CartItem?> {


    @Query("select * from ${OrderModel.CartItem.TABLE_NAME} WHERE uid =:uid")
    abstract fun getAllCart(uid: String?): List<OrderModel.CartItem>

    @Query("select SUM(foodQuantity) from ${OrderModel.CartItem.TABLE_NAME} WHERE uid =:uid")
    abstract fun countItemInCart(uid: String): LiveData<Int?>

    @Query("select SUM((foodQuantity*foodPrice) + (foodQuantity*foodExtraPrice)) from ${OrderModel.CartItem.TABLE_NAME} WHERE uid =:uid")
    abstract fun sumPrice(uid: String): LiveData<Long>

    @Query("select * from ${OrderModel.CartItem.TABLE_NAME} WHERE foodId =:foodId AND uid =:uid")
    abstract fun getItemInCart(foodId: String, uid: String): OrderModel.CartItem?

    @Query("select * from ${OrderModel.CartItem.TABLE_NAME} WHERE foodId =:foodId AND uid =:uid AND foodSize=:foodSize AND foodAddon=:addon")
    abstract fun getItemWithAllOptionsInCart(uid: String, foodId: String, foodSize: String, addon: String): OrderModel.CartItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsertItemInCart(cartItem: OrderModel.CartItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(cartItem: OrderModel.CartItem)

    @Delete
    abstract fun deleteItemInCart(cartItem: OrderModel.CartItem)

    @Query("delete from ${OrderModel.CartItem.TABLE_NAME} WHERE uid =:uid")
    abstract fun cleanCart(uid: String)

}