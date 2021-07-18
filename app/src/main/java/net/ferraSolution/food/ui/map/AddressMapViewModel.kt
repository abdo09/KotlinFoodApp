package net.ferraSolution.food.ui.map

import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.ferraSolution.food.base.BaseViewModel
import net.ferraSolution.food.data.client.network.SingleLiveEvent
import net.ferraSolution.food.data.client.network.UseCaseResult
import net.ferraSolution.food.data.dto.FetchLocationResponse
import net.ferraSolution.food.data.repository.AddressRepository

class AddressMapViewModel(private val userRepository: AddressRepository) : BaseViewModel() {


    var locationResult = SingleLiveEvent<FetchLocationResponse?>()

    val portionLoader = SingleLiveEvent<Boolean>()

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
}
