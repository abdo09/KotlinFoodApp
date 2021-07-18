package net.ferraSolution.food

import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import net.ferraSolution.food.di.appModules
import net.ferraSolution.food.di.dataModule
import net.ferraSolution.food.di.firebaseModule
import net.ferraSolution.food.di.networkModule
import net.ferraSolution.food.utils.Constants
import net.ferraSolution.food.utils.CrashReportingTree
import net.ferraSolution.food.utils.LocalizationUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class FoodApp: MultiDexApplication() {

    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(context)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        //firebase initialization on the app level
        FirebaseApp.initializeApp(this)

        configureTimber()

        configureKoin()

        setDefaultLanguage()

    }

    private fun configureTimber() {
        //Timber used for logging
        // Logging in Debug build, in release log only crashes
        if (BuildConfig.FLAVOR == "development" || BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        else
            Timber.plant(CrashReportingTree())
        Timber.d("BuildConfig.DEBUG ${BuildConfig.DEBUG} ${BuildConfig.BUILD_TYPE} ${BuildConfig.FLAVOR}")
    }

    private fun configureKoin() {
        startKoin {
            fragmentFactory()

            // use the Android context given there
            androidContext(this@FoodApp)
            // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger(Level.ERROR)
            // load properties from assets/koin.properties file
            androidFileProperties()

            modules(listOf(dataModule, firebaseModule, appModules, networkModule))
        }
    }

    private fun setDefaultLanguage() {
        val lang = Constants().getCurrentLanguage(applicationContext)
        LocalizationUtil.setLanguage(lang, this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        setDefaultLanguage()
        super.onConfigurationChanged(newConfig)
        setDefaultLanguage()
    }
}