package net.ferraSolution.food.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.ferraSolution.food.data.models.CategoryModel
import net.ferraSolution.food.data.models.UserModel

@Dao
abstract class UsersDAO : BaseDao<UserModel> {

    @Query("select * from ${UserModel.TABLE_NAME}  limit 1 ")
    abstract fun getUser(): LiveData<UserModel?>

    @Query("delete from ${UserModel.TABLE_NAME}")
    abstract suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsert(userModel: UserModel)
}