package net.ferraSolution.food.data.dto

import android.annotation.SuppressLint
import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Transaction(
    @Json(name="success")
    var id: String? = "",
    @Json(name="status")
    var status: String? = "",
    @Json(name="type")
    var type: String? = "",
    @Json(name="currencyIsoCode")
    var currencyIsoCode: String? = "",
    @Json(name="amount")
    var amount: String? = "",
    @Json(name="merchantAccountId")
    var merchantAccountId: String? = "",
    @Json(name="subMerchantAccountId")
    var subMerchantAccountId: String? = "",
    @Json(name="masterMerchantAccountId")
    var masterMerchantAccountId: String? = "",
    @Json(name="orderId")
    var orderId: String? = "",
    @Json(name="createAt")
    var createAt: String? = "",
    @Json(name="updateAt")
    var updateAt: String? = ""
): Parcelable
