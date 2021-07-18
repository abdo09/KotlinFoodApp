package net.ferraSolution.food.ui.bottomTabs.menu.foodsList

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.ferraSolution.food.data.dao.CartItemDAO
import net.ferraSolution.food.data.dao.MenuDAO
import net.ferraSolution.food.data.models.*
import net.ferraSolution.food.data.repository.HomeRepository
import net.ferraSolution.food.data.repository.MenuRepository
import net.ferraSolution.food.data.repository.roomRepository.CartRoomRepository
import net.ferraSolution.food.data.repository.roomRepository.MenuRoomRepository
import net.ferraSolution.food.data.repository.roomRepository.UserRoomRepository
import net.ferraSolution.food.ui.bottomTabs.menu.MenuFragmentViewModel

class FoodsListFragmentViewModel(
    menuRepository: MenuRepository,
    menuDAO: MenuDAO,
    val cartItemDAO: CartItemDAO,
    menuRoomRepository: MenuRoomRepository,
    private val cartRoomRepository: CartRoomRepository,
    private val userRoomRepository: UserRoomRepository,
    homeRepository: HomeRepository
) : MenuFragmentViewModel(menuRepository, menuDAO, menuRoomRepository, homeRepository) {

    var userMenu: MutableLiveData<UserModel> = MutableLiveData()
    var foods: MutableLiveData<ArrayList<Foods?>> = MutableLiveData()

    suspend fun getLocalUser() = withContext(Dispatchers.IO) {
        userRoomRepository.getLocalUser()
    }

    suspend fun countItemInCart(uid: String) = withContext(Dispatchers.IO) {
        cartRoomRepository.countItemInCart(uid = uid)
    }

    fun getUserMenu(loading: Boolean, uid: String) {
        showLoading.postValue(loading)
        menuRepository.getUserMenu()?.child(uid)
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    showLoading.postValue(false)
                    val model = snapshot.getValue(UserModel::class.java)
                    userMenu.postValue(model)
                }

                override fun onCancelled(error: DatabaseError) {
                    showLoading.postValue(false)
                    showError.postValue(error.message)
                }

            })
    }

    fun updateUserMen(
        uid: String,
        menuId: String,
        foodId: String,
        childToUpdate: String,
        value: Boolean,
    ) {
        menuRepository.updateUserMenu(uid, menuId)
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEachIndexed { index, itemSnapshot ->
                        val model = itemSnapshot.getValue(Foods::class.java)
                        model?.foodKey = itemSnapshot.key ?: ""
                        if (foodId == model?.id) {
                            menuRepository.updateUserMenu(uid, menuId)
                                ?.child(itemSnapshot.key.toString())?.child(childToUpdate)
                                ?.setValue(value)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showLoading.postValue(false)
                    showError.postValue(error.message)
                }

            })
    }

}