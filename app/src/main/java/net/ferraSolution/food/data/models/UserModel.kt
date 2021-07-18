package net.ferraSolution.food.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude

@Entity(tableName = UserModel.TABLE_NAME)
data class UserModel(
    @PrimaryKey
    var uid: String = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var email: String? = "",
    var phoneNumber: String? = "",
    var isMale: Boolean? = true
) {
    companion object {
        const val TABLE_NAME = "users_table"
    }
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
            "phoneNumber" to phoneNumber,
            "isMale" to isMale
        )
    }
}