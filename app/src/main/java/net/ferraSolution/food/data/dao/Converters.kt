package net.ferraSolution.food.data.dao

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.ferraSolution.food.data.models.CategoryModel
import net.ferraSolution.food.data.models.Foods

class Converters {
    @TypeConverter
    fun fromStringToFoods(value: String): List<Foods> {
        val listType = object : TypeToken<List<Foods>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListToString(list: List<Foods>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToCategory(value: String): List<CategoryModel> {
        val listType = object : TypeToken<List<CategoryModel>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromCategoryToString(list: List<CategoryModel>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

}