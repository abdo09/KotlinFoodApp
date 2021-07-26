package net.ferraSolution.food.ui.map

import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.ferraSolution.food.base.BaseViewModel
import net.ferraSolution.food.data.client.network.SingleLiveEvent
import net.ferraSolution.food.data.client.network.UseCaseResult
import net.ferraSolution.food.data.dto.FetchLocationResponse
import net.ferraSolution.food.data.models.Address
import net.ferraSolution.food.data.paths.FireStoreConfig
import net.ferraSolution.food.data.repository.AddressRepository
import net.ferraSolution.food.data.repository.AuthRepository

class AddressMapViewModel(private val userRepository: AddressRepository, private val authRepository: AuthRepository) : BaseViewModel() {


    var locationResult = SingleLiveEvent<FetchLocationResponse?>()

    val portionLoader = SingleLiveEvent<Boolean>()

    val userAddressAdded = SingleLiveEvent<Boolean>()

    var place: Place? = null

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

    fun uploadUserAddress(uid: String, address: Address){
        authRepository.getUserInfo()?.child(uid)?.child(FireStoreConfig.ADDRESS)?.setValue(address)?.addOnCompleteListener {
            if (it.isSuccessful){
                userAddressAdded.postValue(true)
            }
        }
    }
}
