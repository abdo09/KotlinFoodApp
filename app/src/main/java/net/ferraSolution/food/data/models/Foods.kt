package net.ferraSolution.food.data.models

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.firebase.database.PropertyName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Foods(
    var foodKey: String? = "",
    var name: String? = "",
    var image: String? = "",
    var description: String? = "",
    var id: String? = "",
    var addon: List<AddonModel>? = listOf(),
    var size: List<SizeModel>? = listOf(),
    var price: Int? = 0,
    var ratingCount: Long? = 0L,
    var ratingValue: Double? = 0.0,
    var inCart: Boolean? = false,
    var isLiked: Boolean? = false
) : Parcelable