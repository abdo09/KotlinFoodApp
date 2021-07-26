package net.ferraSolution.food.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Marker
import net.ferraSolution.food.R
import kotlinx.android.synthetic.main.address_map_fragment.*
import net.ferraSolution.food.base.BaseSupportFragment
import net.ferraSolution.food.data.client.network.FetchAddressIntentService
import net.ferraSolution.food.utils.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class AddressMapFragment : BaseSupportFragment(R.layout.address_map_fragment), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    companion object {
        const val LOCATION_PERMISSION = 42
        const val SA_LATITUDE: Double = 24.7
        const val SA_LONGITUDE: Double = 46.7
    }

    lateinit var mMap: GoogleMap
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    private var currentLngLat: LatLng? = null
    private var marker: Marker? = null
    private var lastLocation: Location? = null
    private var isGPS = false
    private val locationsList = mutableListOf<String>()
    private val placesList = mutableListOf<String>()
    private var currentPlace: Place? = null

    // Create a new Places client instance.
    private val placesClient by lazy { context?.let { Places.createClient(it) } }

    // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
    // and once again when the user makes a selection (for example when calling fetchPlace()).
    private val token by lazy { AutocompleteSessionToken.newInstance() }

    // Use the builder to create a FindAutocompletePredictionsRequest.
    val adapter by lazy { context?.let { ArrayAdapter<String>(it, R.layout.location_autocomplete_item, locationsList) } }

    override val viewModel: AddressMapViewModel by viewModel()

    val args: AddressMapFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAppBarVisibilityAndTitle(View.VISIBLE, R.string.choose_your_location)
        navigationVisibility = View.GONE
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initViews()
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.google_maps_key))
        }
    }

    private fun initViews() {
        add_address_location.setOnClickListener {
//            if (currentPlace != null)
            navController.navigate(AddressMapFragmentDirections.actionNavAddressDetailsBottomSheetFragment())
            //navController.navigate(AddressMapFragmentDirections.actionAddressMapFragmentToAddressDetailFragment(currentPlace, args.addressId))
            //          else
            //            viewModel.showInfo.postValue(getString(R.string.please_select_alocation))
        }

        /*search_address_location.threshold = 3
        search_address_location.setAdapter(adapter)
        search_address_location.setBackgroundResource(R.color.design_default_color_on_primary)

        val watcher = object : TextWatcherMinified {
            override fun afterTextChanged(s: Editable?) {
                findPlaceByQuery(s.toString())
            }
        }

        search_address_location.addTextChangedListener(watcher)

        search_address_location.setOnItemClickListener { _, view, position, _ ->
            getPositionDetailRequest(placesList[position])
            val keyboardHide = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboardHide.hideSoftInputFromWindow(view.windowToken, 0)

        }*/

        viewModel.locationResult.observe(viewLifecycleOwner, Observer { locationResponse ->
            context?.let { it1 -> toggleKeyboard(it1, false) }
            locationResponse?.let {
                it.results.firstOrNull()?.let { result ->
                    //getPositionDetailRequest(result.placeId, true)
                    Timber.d(result.formattedAddress)
                }
            }
        })

        viewModel.portionLoader.observe(viewLifecycleOwner, Observer {
            if (it)
                loading.fadeIn(500)
            else
                loading.fadeOut(200)
        })

    }


    override fun onMapReady(googleMap: GoogleMap) {
        Timber.d("address - > on map readt ")
        mMap = googleMap
        centerMapInRegion {
            initMap()
            if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
                    == PackageManager.PERMISSION_GRANTED) {
                initMapLocation()
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION
                )
            }
        }
    }


    //center the map in SAUDI ARABIA region if the location permission is not granted
    private fun centerMapInRegion(callback: () -> Unit) {
        Timber.d("centerMapInRegion")
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(SA_LATITUDE, SA_LONGITUDE), 4.0f), object : GoogleMap.CancelableCallback {
            override fun onFinish() {
                callback()
            }

            override fun onCancel() {
                callback()
            }

        })
    }

    override fun onResume() {
        super.onResume()
        try {
            if (this::mMap.isInitialized)
                initMapLocation()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    private fun initMap() {
        Timber.d("address - > init map")
        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED)
            fusedLocationClient?.lastLocation
                    ?.addOnSuccessListener { location: Location? ->
                        location?.let {
                            updateMapLocation(it)
                        }
                    }

        mMap.setOnMapClickListener { latLng ->
            addMarkerToLocation(latLng)
        }


        var currentTarget = mMap.cameraPosition.target

        mMap.setOnCameraMoveListener {
            setLoading()

            mMap.setOnCameraIdleListener {
                if (mMap.cameraPosition.target != currentTarget
                        && mMap.cameraPosition.target.latitude != currentTarget.latitude && mMap.cameraPosition.target.longitude != currentTarget.longitude
                        && mMap.cameraPosition.target.latitude != SA_LATITUDE && mMap.cameraPosition.target.longitude != SA_LATITUDE
                ) {

                    context?.postToLooper(2000){
                        currentTarget = mMap.cameraPosition.target
                        addMarkerToLocation(mMap.cameraPosition.target)
                    }
                }
                mMap.setOnCameraIdleListener(null)
            }
        }

    }

    private fun initMapLocation() {
        Timber.d("address - > init map")
        mMap.apply {
            if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
                    == PackageManager.PERMISSION_GRANTED)
                isMyLocationEnabled = true
            setOnMyLocationButtonClickListener(this@AddressMapFragment)
            setOnMyLocationClickListener(this@AddressMapFragment)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        context?.let {
            GpsUtils(it).turnGPSOn(object : GpsUtils.onGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) { // turn on GPS
                isGPS = isGPSEnable
            }
        })
        }
        fusedLocationClient?.lastLocation
                ?.addOnSuccessListener { location: Location? ->
                    location?.let {
                        updateMapLocation(it)
                    }
                }
        initLocationTracking()
    }

    private fun initLocationTracking() {
        Timber.d("address - > initLocationTracking")
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    updateMapLocation(location)
                }
            }
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            fusedLocationClient?.requestLocationUpdates(
                    LocationRequest(),
                    locationCallback,
                    null)
    }


    private fun getPositionDetail(position: String) {
        //if(isAdded) viewModel.getPlaceDetails(getString(R.string.google_maps_key_http), position)
    }

    private fun getPositionDetailRequest(position: String, isLngLat: Boolean = false) {
        val placeRequest = FetchPlaceRequest
                .builder(position, mutableListOf(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS, Place.Field.NAME, Place.Field.ID))
                .build()
        placesClient?.let { placesClient ->
             placesClient.fetchPlace(placeRequest)
                    .addOnSuccessListener { response ->
                        stopLoading()
                        setPlaceAsSelected(response, isLngLat)
                    }.addOnFailureListener {
                        stopLoading()
                        it.printStackTrace()
                    }
        }

    }

    private fun stopLoading() {
        viewModel.portionLoader.value = false
    }

    private fun setLoading() {
        viewModel.portionLoader.value = true
    }

    private fun setPlaceAsSelected(response: FetchPlaceResponse, isLngLat: Boolean) {
        Timber.d("place $response ")
        ///view?.findViewById<AutoCompleteTextView>(R.id.search_address_location)?.setText(response.place.address)
        currentPlace = response.place
        Timber.d("currentPlace ${response.place} ")
        currentPlace?.latLng?.let {
            if (!isLngLat)
                addMarkerToLocation(it)
        }
    }

    @SuppressLint("TimberArgCount")
    private fun findPlaceByQuery(query: String, isLngLat: Boolean = false) {
        Timber.d("address - > find place by query $query ")
        // val typeFilter = if (isLngLat) TypeFilter.GEOCODE else TypeFilter.ADDRESS
        val request = FindAutocompletePredictionsRequest.builder()
                .setCountry("sa")//Saudi Arabia
                // .setTypeFilter(typeFilter)
                .setSessionToken(token)
                .setQuery(query)
                .build()
        placesClient?.findAutocompletePredictions(request)?.addOnSuccessListener { response ->
            Timber.d("auto complete $response")
            adapter?.clear()
            placesList.clear()
            response.autocompletePredictions.forEach { prediction ->
                placesList.add(prediction.placeId)
                adapter?.add(prediction.getFullText(null).toString())
            }

        }?.addOnFailureListener { exception ->
            if (exception is ApiException) {
                viewModel.showError.value = exception.localizedMessage
                Timber.e("Place not found: %s", exception.statusCode)
                Timber.e("Place Error", exception.localizedMessage)
            }
        }


    }

    private fun addMarkerToLocation(currentLngLat: LatLng) {
        Timber.d("address - > $currentLngLat addingg marker")
        Constants().setLatLng(requireContext(), currentLngLat)

        this.currentLngLat = currentLngLat

        setLoading()

        if (marker != null)
            marker?.setPosition(currentLngLat)
        else {
            marker = mMap.addMarker(
                MarkerOptions().position(currentLngLat).title(resources.getString(R.string.my_location))
                    .draggable(true)
                    .visible(false)
                    .icon(context?.let { MapUtils.bitmapDescriptorFromVector(it, R.drawable.pin_location_icon) }))
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
                currentLngLat.latitude,
                currentLngLat.longitude), 17.0f), object : GoogleMap.CancelableCallback {
            override fun onFinish() {
                getPositionDetail("${currentLngLat.latitude},${currentLngLat.longitude}")
            }

            override fun onCancel() {
                getPositionDetail("${currentLngLat.latitude},${currentLngLat.longitude}")
            }

        })


        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
    }


    private fun updateMapLocation(location: Location) {
        Timber.d("address - > updateMapLocation")
        lastLocation = location
        addMarkerToLocation(LatLng(location.latitude, location.longitude))
    }


    override fun onPause() {
        super.onPause()
        if (locationCallback != null)
            fusedLocationClient?.removeLocationUpdates(locationCallback)
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Timber.d("onActivityResult $requestCode $permissions $grantResults")


        if (requestCode == LOCATION_PERMISSION) {
            if (permissions.isNotEmpty() &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                initMapLocation()
                return
            }
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(requireContext(), "Getting my location", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMyLocationClick(location: Location) {
        addMarkerToLocation(LatLng(location.latitude, location.longitude))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Timber.d("onActivityResult $requestCode $resultCode $data")

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FetchAddressIntentService.GPS_REQUEST) {
                isGPS = true // flag maintain before get location
                initMap()
                initMapLocation()
                return
            }
        }
    }


}
