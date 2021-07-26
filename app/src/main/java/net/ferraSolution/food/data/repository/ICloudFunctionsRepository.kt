package net.ferraSolution.food.data.repository

import net.ferraSolution.food.data.client.network.UseCaseResult
import net.ferraSolution.food.data.dto.BraintreeToken
import net.ferraSolution.food.data.services.ICloudFunctionServices
interface ICloudFunctionsRepository {
    suspend fun refreshToken(): UseCaseResult<BraintreeToken>
}
class ICloudFunctionsRepositoryImp(private val iCloudFunctionServices: ICloudFunctionServices): ICloudFunctionsRepository {
    override suspend fun refreshToken(): UseCaseResult<BraintreeToken> {
        return try {
            UseCaseResult.OnSuccess(iCloudFunctionServices.refreshToken())
        } catch (ex: Exception) {
            UseCaseResult.OnError(ex)
        }
    }

}