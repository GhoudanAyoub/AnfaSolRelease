package ghoudan.anfaSolution.com.app_models

import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(
    val accessToken: String?,
    val expiresIn: Int?,
    val refreshToken: String?,
    val tokenType: String?
)
