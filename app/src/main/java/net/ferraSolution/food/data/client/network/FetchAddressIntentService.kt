package net.ferraSolution.food.data.client.network

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import net.ferraSolution.food.R
import timber.log.Timber
import java.io.IOException
import java.util.*

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * helper methods.
 */
class FetchAddressIntentService : IntentService("FetchAddressIntentService") {

    companion object Constants {
        const val SUCCESS_RESULT = 0
        const val FAILURE_RESULT = 1
        const val PACKAGE_NAME = "net.ferraSolution.food.ui.map"
        const val RECEIVER = "$PACKAGE_NAME.RECEIVER"
        const val RESULT_DATA_KEY = "$PACKAGE_NAME.RESULT_DATA_KEY"
        const val LOCATION_DATA_EXTRA = "$PACKAGE_NAME.LOCATION_DATA_EXTRA"
        const val GPS_REQUEST = 1001
    }

    val TAG = "AddressIntentService"
    private var receiver: ResultReceiver? = null
    override fun onHandleIntent(intent: Intent?) {
        intent ?: return
        var errorMessage = ""
        // Get the location passed to this service through an extra.
        val location = intent.getParcelableExtra<Location?>(LOCATION_DATA_EXTRA) ?: return
        Timber.d(location.toString())
        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address> = emptyList()
        try {
            addresses = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    // In this sample, we get just a single address.
                    1)
        } catch (ioException: IOException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available)
            Timber.e(errorMessage + ioException)
        } catch (illegalArgumentException: IllegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used)
            Timber.e("$errorMessage. Latitude = $location.latitude , " +
                    "Longitude =  $location.longitude" + illegalArgumentException)
        }

        // Handle case where no address was found.
        if (addresses.isEmpty()) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found)
                Timber.e(errorMessage)
            }
            deliverResultToReceiver(FAILURE_RESULT, errorMessage)
        } else {
            Timber.d(addresses.toString())

            val address = addresses[0]
            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            val addressFragments = with(address) {
                (0..maxAddressLineIndex).map { getAddressLine(it) }
            }
            Timber.i(getString(R.string.address_found))
            deliverResultToReceiver(SUCCESS_RESULT,
                    addressFragments.joinToString(separator = "\n"))
        }

    }

    private fun deliverResultToReceiver(resultCode: Int, message: String) {
        val bundle = Bundle().apply { putString(RESULT_DATA_KEY, message) }
        receiver?.send(resultCode, bundle)
    }


}
