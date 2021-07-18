package net.ferraSolution.food.data.models

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ExtrasModel(
    var addon: List<AddonModel>? = listOf(),
    var size: List<SizeModel>? = listOf()
) : Parcelable