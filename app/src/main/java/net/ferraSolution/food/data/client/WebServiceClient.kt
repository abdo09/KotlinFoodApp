package net.ferraSolution.food.data.client

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import net.ferraSolution.food.BuildConfig
import net.ferraSolution.food.config.RemoteConfigs
import okhttp3.Cache
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/* Returns a custom OkHttpClient instance with interceptor. Used for building Retrofit service */
fun createHttpClient(authInterceptor: AuthInterceptor, withInterceptor: Boolean = true): OkHttpClient {
    /* ConnectionSpec.MODERN_TLS is the default value */
    val tlsSpecs: List<ConnectionSpec> = listOf(ConnectionSpec.MODERN_TLS)

    val logInterceptor = HttpLoggingInterceptor()
    logInterceptor.level = HttpLoggingInterceptor.Level.BODY
    val clientBuilder = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectionSpecs(tlsSpecs)
            //.authenticator(Authenticator)
            .addInterceptor {
                val original = it.request()
                Timber.d("okhttp url ${it.request().url}")
                val requestBuilder = original.newBuilder()
                requestBuilder.header("Content-Type", "application/json")
                //requestBuilder.header("Authorization", "bearer $token")
                requestBuilder.header("App-version", BuildConfig.VERSION_NAME)
                val request = requestBuilder
                        .build()
                return@addInterceptor it.proceed(request)
            }
            .callTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)

            .retryOnConnectionFailure(true)
            .cache(Cache(createTempDir(), 200))

    if (withInterceptor)
        clientBuilder.addInterceptor(authInterceptor)

    if (BuildConfig.FLAVOR == "development" || BuildConfig.DEBUG)
        clientBuilder.addInterceptor(logInterceptor)
    return clientBuilder.build()

}


fun createRetrofit(httpClient: OkHttpClient): Retrofit {
    
     val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    return Retrofit.Builder()
            .baseUrl(RemoteConfigs.BASE_URL)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
}


/* function to build our Retrofit service */
inline fun <reified T> createWebService(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}


object UnsafeOkHttpClient {
    // Create a trust manager that does not validate certificate chains
    // Install the all-trusting trust manager
    // Create an ssl socket factory with our all-trusting manager
    val unsafeOkHttpClient: OkHttpClient
        get() {
            try {
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                    }

                    override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                        return arrayOf()
                    }
                })
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, java.security.SecureRandom())
                val sslSocketFactory = sslContext.socketFactory

                val builder = OkHttpClient.Builder()
                builder.readTimeout(60, TimeUnit.SECONDS)
                builder.connectTimeout(60, TimeUnit.SECONDS)
                builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                builder.hostnameVerifier { _, _ -> true }

                return builder.build()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        }
}
