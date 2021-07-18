package net.ferraSolution.food.data.repository.roomRepository

import androidx.lifecycle.LiveData
import net.ferraSolution.food.data.dao.MenuDAO
import net.ferraSolution.food.data.dao.UsersDAO
import net.ferraSolution.food.data.models.CategoryModel
import net.ferraSolution.food.data.models.UserModel

interface UserRoomRepository {

    fun getLocalUser(): LiveData<UserModel?>
    fun upsertUser(userModel: UserModel)

}

class UserRoomRepositoryImp(
    private val usersDAO: UsersDAO
): UserRoomRepository{
    override fun getLocalUser(): LiveData<UserModel?> {
        return usersDAO.getUser()
    }

    override fun upsertUser(userModel: UserModel) {
        usersDAO.upsert(userModel)
    }
}