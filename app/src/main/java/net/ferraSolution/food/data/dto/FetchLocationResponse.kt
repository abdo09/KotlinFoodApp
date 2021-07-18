package net.ferraSolution.food.data.dto


import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class FetchLocationResponse(
        @Json(name="plus_code")
        val plusCode: PlusCode = PlusCode(),
        @Json(name="results")
        val results: List<Result> = listOf(),
        @Json(name="status")
        val status: String = "" // OK
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class PlusCode(
            @Json(name="compound_code")
            val compoundCode: String = "", // P27Q+MC New York, NY, USA
            @Json(name="global_code")
            val globalCode: String = "" // 87G8P27Q+MC
    ) : Parcelable

    @SuppressLint("ParcelCreator")
    @Parcelize
    data class Result(
            @Json(name="address_components")
            val addressComponents: List<AddressComponent> = listOf(),
            @Json(name="formatted_address")
            val formattedAddress: String = "", // United States
            @Json(name="geometry")
            val geometry: Geometry = Geometry(),
            @Json(name="place_id")
            val placeId: String = "", // ChIJCzYy5IS16lQRQrfeQ5K5Oxw
            @Json(name="plus_code")
            val plusCode: PlusCode = PlusCode(),
            @Json(name="types")
            val types: List<String> = listOf()
    ) : Parcelable {
        @SuppressLint("ParcelCreator")
        @Parcelize
        data class AddressComponent(
                @Json(name="long_name")
                val longName: String = "", // United States
                @Json(name="short_name")
                val shortName: String = "", // US
                @Json(name="types")
                val types: List<String> = listOf()
        ) : Parcelable

        @SuppressLint("ParcelCreator")
        @Parcelize
        data class Geometry(
                @Json(name="bounds")
                val bounds: Bounds = Bounds(),
                @Json(name="location")
                val location: Location = Location(),
                @Json(name="location_type")
                val locationType: String = "", // APPROXIMATE
                @Json(name="viewport")
                val viewport: Viewport = Viewport()
        ) : Parcelable {
            @SuppressLint("ParcelCreator")
            @Parcelize
            data class Bounds(
                    @Json(name="northeast")
                    val northeast: Northeast = Northeast(),
                    @Json(name="southwest")
                    val southwest: Southwest = Southwest()
            ) : Parcelable {
                @SuppressLint("ParcelCreator")
                @Parcelize
                data class Northeast(
                        @Json(name="lat")
                        val lat: Double = 0.0, // 71.5388001
                        @Json(name="lng")
                        val lng: Double = 0.0 // -66.885417
                ) : Parcelable

                @SuppressLint("ParcelCreator")
                @Parcelize
                data class Southwest(
                        @Json(name="lat")
                        val lat: Double = 0.0, // 18.7763
                        @Json(name="lng")
                        val lng: Double = 0.0 // 170.5957
                ) : Parcelable
            }

            @SuppressLint("ParcelCreator")
            @Parcelize
            data class Location(
                    @Json(name="lat")
                    val lat: Double = 0.0, // 37.09024
                    @Json(name="lng")
                    val lng: Double = 0.0 // -95.712891
            ) : Parcelable

            @SuppressLint("ParcelCreator")
            @Parcelize
            data class Viewport(
                    @Json(name="northeast")
                    val northeast: Northeast = Northeast(),
                    @Json(name="southwest")
                    val southwest: Southwest = Southwest()
            ) : Parcelable {
                @SuppressLint("ParcelCreator")
                @Parcelize
                data class Northeast(
                        @Json(name="lat")
                        val lat: Double = 0.0, // 71.5388001
                        @Json(name="lng")
                        val lng: Double = 0.0 // -66.885417
                ) : Parcelable

                @SuppressLint("ParcelCreator")
                @Parcelize
                data class Southwest(
                        @Json(name="lat")
                        val lat: Double = 0.0, // 18.7763
                        @Json(name="lng")
                        val lng: Double = 0.0 // 170.5957
                ) : Parcelable
            }
        }

        @SuppressLint("ParcelCreator")
        @Parcelize
        data class PlusCode(
                @Json(name="compound_code")
                val compoundCode: String = "", // P27Q+MF New York, United States
                @Json(name="global_code")
                val globalCode: String = "" // 87G8P27Q+MF
        ) : Parcelable
    }
}