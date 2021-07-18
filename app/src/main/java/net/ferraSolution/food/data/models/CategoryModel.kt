package net.ferraSolution.food.data.models

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize
import net.ferraSolution.food.data.models.CategoryModel.Companion.TABLE_NAME

@SuppressLint("ParcelCreator")
@Parcelize
@Entity(tableName = TABLE_NAME)
data class CategoryModel(
    var categoryKey: String? = "",
    var name: String? = "",
    var image: String? = "",
    var foods: List<Foods>? = listOf(),
    @PrimaryKey
    var menuId: String = ""
): Parcelable{

    companion object {
        const val TABLE_NAME = "category_table"
    }
}
