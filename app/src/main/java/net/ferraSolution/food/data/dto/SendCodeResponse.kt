package com.abdo.baseProject.data.dto

import com.squareup.moshi.Json


class SendCodeResponse {
    @Json(name = "case")
    var case = 0

    @Json(name = "message")
    var message: String? = null

    @Json(name = "accessToken")
    var accessToken: String? = null

    @Json(name = "refreshToken")
    var refreshToken: String? = null

    @Json(name = "accessTokenExpiresAfter")
    var accessTokenExpiresAfter: Long? = null
}
