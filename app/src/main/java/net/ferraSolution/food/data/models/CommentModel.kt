package net.ferraSolution.food.data.models


import android.annotation.SuppressLint
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class CommentModel(
    var comment: String? = "",
    var commentTimeStamp: CommentTimeStamp? = CommentTimeStamp(),
    var name: String? = "", // Abdalrahman Omer
    var ratingBarValue: Int? = 0, // 5
    var uid: String? = "" // 0adQGcc5Eteo6OCIQOqYNfBKxbk1
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    data class CommentTimeStamp(
        var timeStamp: Long? = 0 // 1624552889761
    ) : Parcelable
}