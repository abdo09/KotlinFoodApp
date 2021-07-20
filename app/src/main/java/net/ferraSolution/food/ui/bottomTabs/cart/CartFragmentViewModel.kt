package net.ferraSolution.food.ui.bottomTabs.cart

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.ferraSolution.food.base.BaseViewModel
import net.ferraSolution.food.data.dao.CartItemDAO
import net.ferraSolution.food.data.models.OrderModel
import net.ferraSolution.food.data.repository.roomRepository.CartRoomRepository

class CartFragmentViewModel(val cartRoomRepository: CartRoomRepository): BaseViewModel() {
    var allCart: MutableLiveData<List<OrderModel.CartItem>> = MutableLiveData<List<OrderModel.CartItem>>()

    suspend fun getAllCart(uid: String?) = withContext(Dispatchers.IO) {
        val cart = cartRoomRepository.getAllCartFromDB(uid)
        allCart.postValue(cart)
    }
}