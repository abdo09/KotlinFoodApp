package net.ferraSolution.food.data.repository.roomRepository

import androidx.lifecycle.LiveData
import net.ferraSolution.food.data.dao.MenuDAO
import net.ferraSolution.food.data.models.CategoryModel

interface MenuRoomRepository {

    fun getAllCategoriesFromDB(): LiveData<List<CategoryModel>>
    fun upsertCategory(categoryModel: CategoryModel)

}

class MenuRoomRepositoryImp(
    private val menuDAO: MenuDAO
): MenuRoomRepository{
    override fun getAllCategoriesFromDB(): LiveData<List<CategoryModel>> {
        return menuDAO.getAllCategories()
    }

    override fun upsertCategory(categoryModel: CategoryModel){
        return menuDAO.upsert(categoryModel)
    }

}