package net.ferraSolution.food.data.models

import android.annotation.SuppressLint
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import androidx.room.Entity
import net.ferraSolution.food.data.models.OrderModel.CartItem.Companion.TABLE_NAME

@SuppressLint("ParcelCreator")
@Parcelize
data class OrderModel(
    var cartItemList: List<CartItem?>? = listOf(),
    var cod: Boolean? = false, // true
    var comment: String? = "",
    var discount: Int? = 0, // 0
    var finalPayment: Int? = 0, // 9
    var lat: Double? = 0.0, // 30.0700718
    var lng: Double? = 0.0, // 31.3443691
    var shippingAddress: String? = "", // 30.0700718/31.3443691Makrem Ebeid Ext, Masaken Al Mohandesin, Nasr City, Cairo Governorate, Egypt
    var totalPayment: Int? = 0, // 9
    var transactionId: String? = "", // Cash On Delivery
    var userId: String? = "", // 0adQGcc5Eteo6OCIQOqYNfBKxbk1
    var userPhone: String? = "", // +201148018005
    var userName: String? = "", // Abdalrahman Omer
    var orderTimeStamp: String? = ""
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Entity(tableName = TABLE_NAME, primaryKeys = ["uid", "foodId", "foodSize", "foodAddon"])
    data class CartItem(
        var foodAddon: String = "", // [{"name":"Rice","price":2}]
        var foodExtraPrice: Double? = 0.0, // 4
        var foodId: String = "", // chicken_01
        var foodImage: String? = "", // https://live.staticflickr.com/65535/48580764482_ae213e429e_o.jpg
        var foodName: String? = "", // ROASTED QUARTER CHICKEN
        var foodPrice: Int? = 0, // 5
        var foodQuantity: Int = 0, // 1
        var foodSize: String = "", // []
        var uid: String = "", // 0adQGcc5Eteo6OCIQOqYNfBKxbk1
        var userPhone: String? = "" // +201148018005
    ) : Parcelable {
        companion object {
            const val TABLE_NAME = "cart_item_table"
        }

        override fun equals(other: Any?): Boolean {
            if (other === this) return true
            if (other !is CartItem) return false
            val cartItem = other as CartItem?
            return cartItem?.foodId == this.foodId &&
                    cartItem.foodAddon == this.foodAddon &&
                    cartItem.foodSize == this.foodSize
        }
    }
}