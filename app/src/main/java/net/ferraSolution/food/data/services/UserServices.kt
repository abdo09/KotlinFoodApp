package net.ferraSolution.food.data.services

import com.abdo.baseProject.data.dto.SendCodeResponse
import net.ferraSolution.food.data.dto.FetchLocationResponse
import retrofit2.http.*

interface UserServices {

    @PUT("api/refreshToken")
    suspend fun refreshToken(@Body body: Map<String, String>): SendCodeResponse

    @GET
    suspend fun getLocationDetails(@Url url: String, @Query(value = "key", encoded = true) key: String, @Query(value = "latlng", encoded = true) latlng: String): FetchLocationResponse

}