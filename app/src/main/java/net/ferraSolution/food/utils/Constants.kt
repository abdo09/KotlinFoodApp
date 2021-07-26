package net.ferraSolution.food.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.ferraSolution.food.R
import net.ferraSolution.food.data.models.AddonModel
import net.ferraSolution.food.data.models.Address
import net.ferraSolution.food.data.models.SizeModel
import net.ferraSolution.food.data.models.UserModel
import net.ferraSolution.food.di.getSharedPrefs
import java.lang.reflect.Type


class Constants {

    fun firstTime(context: Context): Boolean {
        return getSharedPrefs(context).getBoolean(context.getString(R.string.pref_first_time), false)
    }

    fun firstTime(context: Context, isFirstTime: Boolean) {
        getSharedPrefs(context).edit().putBoolean(context.getString(R.string.pref_first_time), isFirstTime).apply()
    }

    fun getCurrentLanguage(context: Context): String? {
        return getSharedPrefs(context).getString(context.getString(R.string.pref_language), "ar")
    }

    fun setCurrentLanguage(context: Context, lang: String) {
        getSharedPrefs(context).edit().putString(context.getString(R.string.pref_language), lang).apply()
    }

    fun getUid(context: Context): String? {
        return getSharedPrefs(context).getString(context.getString(R.string.pref_uid), "")
    }

    fun setUid(context: Context, uid: String) {
        getSharedPrefs(context).edit().putString(context.getString(R.string.pref_uid), uid).apply()
    }

    fun setAddons(context: Context, addonModel: List<AddonModel>) {
        val gson = Gson()
        val json = gson.toJson(addonModel)
        getSharedPrefs(context).edit().putString(context.getString(R.string.pref_addon), json).apply()
    }

    fun getAddons(context: Context): List<*>? {
        val gson = Gson()
        val json = getSharedPrefs(context).getString(context.getString(R.string.pref_addon), null)
        val type: Type = object : TypeToken<List<AddonModel>?>() {}.type
        return gson.fromJson<Any>(json, type) as List<*>?
    }

    fun getAccessToken(context: Context): String? {
        return getSharedPrefs(context).getString(context.getString(R.string.pref_access_token), "")
    }


    fun setAccessToken(context: Context, accessToken: String?) {
        getSharedPrefs(context).edit().putString(context.getString(R.string.pref_access_token), accessToken).apply()
    }

    fun getRefreshToken(context: Context): String? {
        return getSharedPrefs(context).getString(context.getString(R.string.pref_refresh_token), "")
    }

    fun setRefreshToken(context: Context, refreshToken: String?) {
        getSharedPrefs(context).edit().putString(context.getString(R.string.pref_refresh_token), refreshToken).apply()
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String? {
        return getSharedPrefs(context).getString(context.getString(R.string.pref_device_id), Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
    }

    fun setDeviceId(context: Context, mobileNumber: String?) {
        getSharedPrefs(context).edit().putString(context.getString(R.string.pref_device_id), mobileNumber).apply()
    }

    fun setSize(context: Context, sizeModel: List<SizeModel>) {
        val gson = Gson()
        val json = gson.toJson(sizeModel)
        getSharedPrefs(context).edit().putString(context.getString(R.string.pref_size), json).apply()
    }

    fun getSize(context: Context): List<*>? {
        val gson = Gson()
        val json = getSharedPrefs(context).getString(context.getString(R.string.pref_size), null)
        val type: Type = object : TypeToken<List<SizeModel>?>() {}.type
        return gson.fromJson<Any>(json, type) as List<*>?
    }

    fun setUser(context: Context, userModel: UserModel?) {
        val gson = Gson()
        val json = gson.toJson(userModel)
        getSharedPrefs(context).edit().putString(context.getString(R.string.pref_user), json).apply()
    }

    fun getUser(context: Context): UserModel? {
        val gson = Gson()
        val json = getSharedPrefs(context).getString(context.getString(R.string.pref_user), null)
        val type: Type = object : TypeToken<UserModel?>() {}.type
        return gson.fromJson<Any>(json, type) as UserModel?
    }

    fun setLatLng(context: Context, latLng: LatLng) {
        val gson = Gson()
        val json = gson.toJson(latLng)
        getSharedPrefs(context).edit().putString(context.getString(R.string.pref_lat_lang), json).apply()
    }

    fun getLatLng(context: Context): LatLng? {
        val gson = Gson()
        val json = getSharedPrefs(context).getString(context.getString(R.string.pref_lat_lang), null)
        val type: Type = object : TypeToken<LatLng?>() {}.type
        return gson.fromJson<Any>(json, type) as LatLng?
    }

    fun setAddress(context: Context, address: Address) {
        val gson = Gson()
        val json = gson.toJson(address)
        getSharedPrefs(context).edit().putString(context.getString(R.string.pref_address), json).apply()
    }

    fun getAddress(context: Context): Address? {
        val gson = Gson()
        val json = getSharedPrefs(context).getString(context.getString(R.string.pref_address), null)
        val type: Type = object : TypeToken<Address?>() {}.type
        return gson.fromJson<Any>(json, type) as Address?
    }

}

