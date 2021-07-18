package net.ferraSolution.food.data.client

import android.app.Application
import com.auth0.android.jwt.JWT
import net.ferraSolution.food.utils.Constants
import okhttp3.*
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Retrofit
import timber.log.Timber
import java.lang.Exception

class AuthInterceptor(val context: Application) : Interceptor {
    private val retrofit by inject(Retrofit::class.java, named("noauth"))
    private var refreshed = false
    private val deviceId = Constants().getDeviceId(context) ?: ""
    private val minutesToRefresh: Long = (60 * 5)
    override fun intercept(chain: Interceptor.Chain): Response {
        val mToken = Constants().getAccessToken(context) ?: ""
        val refreshToken = Constants().getRefreshToken(context) ?: ""

        val jtw = try {
            JWT(mToken)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }

        Timber.d("bearer ${jtw?.expiresAt}")
        Timber.d("bearer ${jtw?.isExpired(minutesToRefresh)}")
        Timber.d("bearer $deviceId")
        // This is a synchronous call
        val request = chain.request().newBuilder()
            .addHeaders(mToken)
            .build()
        return chain.proceed(request)
        /*val updatedToken = if (jtw?.isExpired(minutesToRefresh) == true) getNewToken(refreshToken) else mToken
        val request = chain.request().newBuilder()
                .addHeaders(updatedToken)
                .build()
        return chain.proceed(request)*/
    }

    /*private fun getNewToken(refreshToken: String): String {
        if (refreshed)
            return Constants().getAccessToken(context) ?: ""
        var newToken = ""
        Timber.d("bearer getting $newToken")
        return runBlocking {
            launch {
                val authTokenResponse = withContext(Dispatchers.IO) {
                    refreshToken(refreshToken = refreshToken, deviceId = deviceId)
                }
                when (authTokenResponse) {
                    is UseCaseResult.OnSuccess -> {

                        Constants().apply {
                            setAccessToken(context, authTokenResponse.data.accessToken)
                            setRefreshToken(context, authTokenResponse.data.refreshToken)
                        }
                        newToken = authTokenResponse.data.accessToken ?: ""
                    }
                    is UseCaseResult.OnError -> {
                        Timber.d("bearer error ${authTokenResponse.exception.localizedMessage}")
                        authTokenResponse.exception.printStackTrace()
                    }

                }
            }
            return@runBlocking newToken
        }
    }*/


    private fun Request.Builder.addHeaders(token: String?) = this.apply { header("Authorization", "Bearer $token") }

    /*private suspend fun refreshToken(refreshToken: String, deviceId: String): UseCaseResult<SendCodeResponse> {
        val userServices = retrofit.create(UserServices::class.java)
        val params = hashMapOf(
                "deviceId" to deviceId,
                "refreshToken" to refreshToken
        )
        Timber.d("bearer params $params")
        return try {
            val otpResult = userServices.refreshToken(params)
            Timber.d("bearer params $otpResult")
            UseCaseResult.OnSuccess(otpResult)
        } catch (ex: Exception) {
            UseCaseResult.OnError(ex)
        }
    }*/

}