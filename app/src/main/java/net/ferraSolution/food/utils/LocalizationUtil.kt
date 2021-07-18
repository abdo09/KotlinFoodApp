package net.ferraSolution.food.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.preference.PreferenceManager
import net.ferraSolution.food.R
import java.util.*


object LocalizationUtil {

    fun setLanguage(lang: String?, context: Context) {
        if (lang != null) {
            val config = Configuration(context.resources.configuration)
            val myLocale = Locale(lang)
            Locale.setDefault(myLocale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Locale.setDefault(Locale.Category.DISPLAY, myLocale)
            }
            config.setLocale(myLocale)
            config.setLayoutDirection(myLocale)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            /*TypeFaceUtil.replaceFont(Typeface.createFromAsset(context.assets, context.resources.getString(R.string.font_regular)))
            TypeFaceUtil.overrideFont(context, "SERIF", context.resources.getString(R.string.font_regular))
            TypeFaceUtil.overrideFont(context, "monospace", context.resources.getString(R.string.font_regular))
            TypeFaceUtil.overrideFont(context, "serif-monospace", context.resources.getString(R.string.font_regular))*/
        }
    }


    fun onAttach(context: Context): Context? {
        val locale = getPersistedLocale(context)
        return setLocale(context, locale)
    }

    fun getPersistedLocale(context: Context): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(context.resources.getString(R.string.pref_language), "en")
    }

    /**
     * Set the app's locale to the one specified by the given String.
     *
     * @param context
     * @param localeSpec a locale specification as used for Android resources (NOTE: does not
     * support country and variant codes so far); the special string "system" sets
     * the locale to the locale specified in system settings
     * @return
     */
    fun setLocale(context: Context, localeSpec: String?): Context? {
        val locale: Locale = if (localeSpec == "system") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Resources.getSystem().configuration.locales.get(0)
            } else {
                Resources.getSystem().configuration.locale
            }
        } else {
            Locale(localeSpec)
        }
        Locale.setDefault(locale)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, locale)
        } else {
            updateResourcesLegacy(context, locale)
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, locale: Locale): Context? {
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, locale: Locale): Context? {
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

}
