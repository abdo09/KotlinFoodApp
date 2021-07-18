package net.ferraSolution.food.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import net.ferraSolution.food.data.models.CategoryModel

@Dao
abstract class MenuDAO : BaseDao<CategoryModel?> {


    @Query("select * from ${CategoryModel.TABLE_NAME}")
    abstract fun getAllCategories(): LiveData<List<CategoryModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsert(categoryModel: CategoryModel)

}