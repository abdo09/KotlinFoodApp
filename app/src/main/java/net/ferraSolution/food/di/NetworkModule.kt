package net.ferraSolution.food.di


import net.ferraSolution.food.data.client.createHttpClient
import net.ferraSolution.food.data.client.createRetrofit
import net.ferraSolution.food.data.client.createWebService
import net.ferraSolution.food.data.client.AuthInterceptor
import net.ferraSolution.food.data.services.UserServices
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module


val networkModule = module {
    //create webservices definitions
    single {
        AuthInterceptor(androidApplication())
    }
    single {
        createHttpClient(get())
    }
    single(named("noauth")) {
        createHttpClient(get(), false)
    }

    single { createRetrofit(get()) }

    single(named("noauth")) {
        val httpClient by inject<OkHttpClient>(named("noauth"))
        createRetrofit(httpClient)
    }
    factory { createWebService<UserServices>(get()) }
}

