package net.ferraSolution.food.data.dto

import android.annotation.SuppressLint
import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class BraintreeTransaction(
    @Json(name="success")
    val success: Boolean = false, // OK
    @Json(name="transaction")
    val transaction: Transaction = Transaction() // OK
): Parcelable
