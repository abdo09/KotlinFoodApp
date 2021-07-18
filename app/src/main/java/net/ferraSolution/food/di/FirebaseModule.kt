package net.ferraSolution.food.di

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val firebaseModule = module {

    single {
        val foodAppOptions = FirebaseOptions.Builder()
                .setApplicationId("1:506411955069:android:ba6a5baef945040f465214")
                .setProjectId("ferra-taga-5dbd4")
                .setApiKey("AIzaSyC6UCEcBh69s_oiEsmmXxcQ-ZhQKjNMgLM")
                .build()
        FirebaseApp.initializeApp(androidApplication(), foodAppOptions, "food_application")
    }

    //firebase module

    single {
        FirebaseFirestore.getInstance(get())
    }

    single {
        FirebaseDatabase.getInstance()
    }

    single {
        FirebaseAuth.getInstance(get())
    }

}