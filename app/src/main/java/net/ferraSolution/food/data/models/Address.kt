package net.ferraSolution.food.data.models

import com.google.android.gms.maps.model.LatLng
import net.ferraSolution.food.ui.map.AddressMapFragment.Companion.SA_LATITUDE
import net.ferraSolution.food.ui.map.AddressMapFragment.Companion.SA_LONGITUDE

data class Address(
    var latLang: LatLng? = LatLng(SA_LATITUDE, SA_LONGITUDE),
    var city: String? = "",
    var street: String? = "",
    var district: String? = "",
    var buildingNo: String? = ""
)
