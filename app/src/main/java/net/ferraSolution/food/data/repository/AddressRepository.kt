package net.ferraSolution.food.data.repository

import net.ferraSolution.food.config.RemoteConfigs
import net.ferraSolution.food.data.client.network.UseCaseResult
import net.ferraSolution.food.data.dto.FetchLocationResponse
import net.ferraSolution.food.data.services.UserServices

interface AddressRepository {
    suspend fun getLocationDetails(key: String, location: String): UseCaseResult<FetchLocationResponse>
}
class AddressRepositoryImp(private val userServices: UserServices): AddressRepository {
    override suspend fun getLocationDetails(
        key: String,
        location: String
    ): UseCaseResult<FetchLocationResponse> {
        return try {
            UseCaseResult.OnSuccess(userServices.getLocationDetails(
                RemoteConfigs.GOOGLE_MAP_URL,
                key, location))
        } catch (ex: Exception) {
            UseCaseResult.OnError(ex)
        }
    }
}