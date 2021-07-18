package net.ferraSolution.food.base

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.findNavController
import net.ferraSolution.food.R
import net.ferraSolution.food.utils.ContextWrapper
import net.ferraSolution.food.utils.LocalizationUtil
import net.ferraSolution.food.utils.getCustomColor
import net.ferraSolution.food.utils.navigationBarAndStatusBarColor
import saschpe.android.customtabs.CustomTabsHelper
import saschpe.android.customtabs.WebViewFallback
import java.util.*

abstract class BaseSupportActivity: AppCompatActivity() {

    val navController by lazy { findNavController(R.id.foodNavHostFragment) }

    override fun attachBaseContext(newBase: Context?) {
        val lang = "en"
        val myLocale = Locale(lang)
        val context: Context = ContextWrapper.wrap(newBase, myLocale)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        activeLocalization()
        super.onCreate(savedInstanceState)
        activeLocalization()

        this.navigationBarAndStatusBarColor(R.color.appColor, R.color.appColor)

    }

    override fun onStart() {
        super.onStart()
        activeLocalization()
    }

    override fun onResume() {
        super.onResume()
        activeLocalization()
    }

    fun activeLocalization() {
        val preferences = getSharedPreferences(getString(R.string.pref_language), Context.MODE_PRIVATE)
        val lang = preferences.getString(getString(R.string.lang_key), "en") ?: "en"
        LocalizationUtil.setLanguage(lang, this)
    }

    fun launchWebUrl(url: String) {
        val intentBuilder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
        val params : CustomTabColorSchemeParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(this.getCustomColor(R.color.appHardColor))
            .build()
        intentBuilder.setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, params)
        intentBuilder.setShowTitle(false)
        intentBuilder.setCloseButtonIcon(ResourcesCompat.getDrawable(resources ,R.drawable.arrow_right, this.theme)!!.toBitmap())
        intentBuilder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
        intentBuilder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right)

        val customTabsIntent: CustomTabsIntent = intentBuilder.build()

        // This is optional but recommended
        CustomTabsHelper.addKeepAliveExtra(this, customTabsIntent.intent)

        // This is where the magic happens...
        CustomTabsHelper.openCustomTab(this, customTabsIntent,
            Uri.parse(url),
            WebViewFallback()
        )
    }

}