package net.ferraSolution.food.data.models

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
    @Parcelize
    data class SizeModel(
        var name: String? = "",
        var price: Int = 0,
        var taken: Boolean? = false
    ) : Parcelable