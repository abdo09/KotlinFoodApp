package net.ferraSolution.food.ui.map

import androidx.lifecycle.MutableLiveData
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.ferraSolution.food.base.BaseViewModel
import net.ferraSolution.food.data.client.network.SingleLiveEvent
import net.ferraSolution.food.data.client.network.UseCaseResult
import net.ferraSolution.food.data.dao.CartItemDAO
import net.ferraSolution.food.data.dto.BraintreeToken
import net.ferraSolution.food.data.dto.FetchLocationResponse
import net.ferraSolution.food.data.models.Address
import net.ferraSolution.food.data.models.OrderModel
import net.ferraSolution.food.data.paths.FireStoreConfig
import net.ferraSolution.food.data.repository.AddressRepository
import net.ferraSolution.food.data.repository.AuthRepository
import net.ferraSolution.food.data.repository.ICloudFunctionsRepository
import net.ferraSolution.food.data.repository.roomRepository.CartRoomRepository

class AddressMapViewModel(
    private val userRepository: AddressRepository,
    private val authRepository: AuthRepository,
    val cartRoomRepository: CartRoomRepository,
    private val iCloudFunctionsRepository: ICloudFunctionsRepository

) : BaseViewModel() {

    var locationResult = SingleLiveEvent<FetchLocationResponse?>()
    val portionLoader = SingleLiveEvent<Boolean>()
    val userAddressAdded = SingleLiveEvent<Boolean>()
    var place: Place? = null
    var braintreeToken = SingleLiveEvent<BraintreeToken>()
    var cartList: List<OrderModel.CartItem> = listOf()
    var orderPlaced: MutableLiveData<Boolean> = MutableLiveData()

    suspend fun getAllCart(uid: String?) = withContext(Dispatchers.IO) {
        val cart = cartRoomRepository.getAllCartFromDB(uid)
        cartList = cart
    }

    fun uploadUserAddress(uid: String, address: Address) {
        authRepository.getUserInfo()?.child(uid)?.child(FireStoreConfig.ADDRESS)?.setValue(address)
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    userAddressAdded.postValue(true)
                }
            }
    }

    fun placeOrder(orderNumber: String, orderModel: OrderModel) {
        showLoading.postValue(true)
        authRepository.orders()?.child(orderNumber)?.setValue(orderModel)
            ?.addOnFailureListener { showLoading.postValue(false)
                showError.postValue(it.message) }
            ?.addOnCompleteListener {
                showLoading.postValue(false)
                if (it.isSuccessful) {
                    orderPlaced.postValue(true)
                }
            }
    }

    fun getToken(){
        launch {
            when (val result =  withContext(Dispatchers.IO){ iCloudFunctionsRepository.refreshToken() }) {
                is UseCaseResult.OnSuccess -> { braintreeToken.postValue(result.data) }
                is UseCaseResult.OnError -> { showError.value = result.exception.message }
            }
        }
    }

    fun getPlaceDetails(key: String, location: String) {

        // Show progressBar during the operation on the MAIN (default) thread
        portionLoader.value = true
        // launch the Coroutine
        launch {
            val getLocationDetails = withContext(Dispatchers.IO) {
                userRepository.getLocationDetails(key, location)
            }
            when (getLocationDetails) {
                is UseCaseResult.OnSuccess -> locationResult.postValue(getLocationDetails.data)
                is UseCaseResult.OnError -> showError.value = getLocationDetails.exception.message
            }

        }
    }
}
