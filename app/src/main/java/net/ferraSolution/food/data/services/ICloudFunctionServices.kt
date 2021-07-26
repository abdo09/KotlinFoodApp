package net.ferraSolution.food.data.services

import net.ferraSolution.food.data.dto.BraintreeToken
import net.ferraSolution.food.data.dto.BraintreeTransaction
import net.ferraSolution.food.data.dto.SendCodeResponse
import net.ferraSolution.food.data.dto.FetchLocationResponse
import retrofit2.http.*

interface ICloudFunctionServices {

    @GET("token")
    suspend fun refreshToken(): BraintreeToken

    @POST("checkout")
    @FormUrlEncoded
    suspend fun submitPayment(@Field("amount") amount: Double,
    @Field("payment_method_nonce")  nonce: String): BraintreeTransaction

}