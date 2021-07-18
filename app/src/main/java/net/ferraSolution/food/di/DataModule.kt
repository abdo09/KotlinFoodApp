package net.ferraSolution.food.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import net.ferraSolution.food.data.FerraFoodDB
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val dataModule = module {

    // FerraFoodDB database instance
    single {
        Room.databaseBuilder(androidApplication(), FerraFoodDB::class.java,
            "ferra_food_data.db")
            .fallbackToDestructiveMigration()
            //.fallbackToDestructiveMigrationOnDowngrade()
            .build()
    }

    //get shared preferences
    single {
        getSharedPrefs(get())
    }

    single<SharedPreferences.Editor> {
        getSharedPrefs(get()).edit()
    }

    //todo refactor repo for both of them
    single { get<FerraFoodDB>().menuDAO() }
    single { get<FerraFoodDB>().userDAO() }
    single { get<FerraFoodDB>().cartItemDAO() }

}


fun getSharedPrefs(context: Context): SharedPreferences {
    return context.getSharedPreferences("default", Context.MODE_PRIVATE)
}


