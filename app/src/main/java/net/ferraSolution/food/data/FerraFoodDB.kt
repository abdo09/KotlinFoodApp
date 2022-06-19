package net.ferraSolution.food.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.ferraSolution.food.data.dao.CartItemDAO
import net.ferraSolution.food.data.dao.Converters
import net.ferraSolution.food.data.dao.MenuDAO
import net.ferraSolution.food.data.dao.UsersDAO
import net.ferraSolution.food.data.models.CategoryModel
import net.ferraSolution.food.data.models.OrderModel
import net.ferraSolution.food.data.models.UserModel

@Database(
    entities = [
        CategoryModel::class,
        UserModel::class,
        OrderModel.CartItem::class,
    ], version = 7, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FerraFoodDB : RoomDatabase() {
    abstract fun menuDAO(): MenuDAO
    abstract fun userDAO(): UsersDAO
    abstract fun cartItemDAO(): CartItemDAO
}