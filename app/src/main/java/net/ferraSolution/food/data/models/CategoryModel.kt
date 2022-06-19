package net.ferraSolution.food.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import net.ferraSolution.food.data.models.CategoryModel.Companion.TABLE_NAME

@Parcelize
@Entity(tableName = TABLE_NAME)
data class CategoryModel(
    var categoryKey: String = "",
    var name: String = "",
    var image: String = "",
    var foods: List<Foods> = listOf(),
    @PrimaryKey
    var menuId: String = ""
): Parcelable{

    companion object {
        const val TABLE_NAME = "category_table"
    }
}
