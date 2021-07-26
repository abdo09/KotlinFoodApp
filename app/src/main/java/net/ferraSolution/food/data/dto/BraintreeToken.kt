package net.ferraSolution.food.data.dto

import android.annotation.SuppressLint
import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class BraintreeToken(
    @Json(name="error")
    val error: Boolean = false, // OK
    @Json(name="token")
    val token: String = "" // OK
): Parcelable
