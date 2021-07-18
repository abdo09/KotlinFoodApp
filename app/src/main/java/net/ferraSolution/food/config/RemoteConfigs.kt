package net.ferraSolution.food.config

import net.ferraSolution.food.BuildConfig

class RemoteConfigs private constructor() {
    companion object {
        //testing
        val BASE_URL = if (BuildConfig.FLAVOR == "development") BuildConfig.SERVER_TEST else BuildConfig.SERVER_URL
        const val GOOGLE_MAP_URL = "https://maps.googleapis.com/maps/api/geocode/json"
    }

}
