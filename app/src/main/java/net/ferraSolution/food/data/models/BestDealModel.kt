package net.ferraSolution.food.data.models

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class BestDealModel(
    var categoryId: String? = "",
    var menu_id: String? = "",
    var food_id: String? = "",
    var name: String? = "",
    var image: String? = ""
): Parcelable
